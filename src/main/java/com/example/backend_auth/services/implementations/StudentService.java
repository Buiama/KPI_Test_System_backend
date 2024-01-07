package com.example.backend_auth.services.implementations;

import com.example.backend_auth.DTOs.requests.ChangePasswordRequest;
import com.example.backend_auth.DTOs.requests.RunCodeRequest;
import com.example.backend_auth.DTOs.requests.SaveCodeRequest;
import com.example.backend_auth.DTOs.responses.CodeResponse;
import com.example.backend_auth.DTOs.responses.ExecutedCodeResponse;
import com.example.backend_auth.exceptions.StudentNotFoundException;
import com.example.backend_auth.exceptions.WrongPasswordException;
import com.example.backend_auth.models.Code;
import com.example.backend_auth.models.ConfirmationToken;
import com.example.backend_auth.models.Student;
import com.example.backend_auth.repositories.IStudentRepository;
import com.example.backend_auth.services.interfaces.ICodeService;
import com.example.backend_auth.services.interfaces.IConfirmationTokenService;
import com.example.backend_auth.services.interfaces.IStudentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentService implements UserDetailsService, IStudentService {
    private final ICodeService codeService;
    private final IStudentRepository studentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IConfirmationTokenService confirmationTokenService;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return studentRepository.findByEmail(email)
                .orElseThrow(()->new StudentNotFoundException(email));
    }

    public String signUp(Student student) {
        String encodedPassword = bCryptPasswordEncoder.encode(student.getPassword());
        student.setPassword(encodedPassword);
        studentRepository.save(student);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken =
                new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), student);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }
    public void changePassword(ChangePasswordRequest request) {
        Student student = studentRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new StudentNotFoundException(request.getEmail()));
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), student.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationNewPassword())) {
            throw new WrongPasswordException("Password are not the same");
        }

        student.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        studentRepository.save(student);
    }
    public void saveCode(SaveCodeRequest request) {
        Student student = studentRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new StudentNotFoundException(request.getEmail()));
        codeService.saveCode(new Code(request.getLanguage(), request.getCode(), student));
    }
    public List<CodeResponse> getAllCodes() {
        return codeService.getAllCodes();
    }
    public ExecutedCodeResponse runCode(RunCodeRequest request) {
        return codeService.executeCode(request.getLanguage(), request.getCode());
    }
    public void enableStudent(String email) {
        Optional<Student> student = studentRepository.findByEmail(email);
        student.ifPresent(value -> value.setEnabled(true));
        studentRepository.enableStudent(email);
    }
    public String resendEmail(Student student) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken =
                new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), student);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }
}
