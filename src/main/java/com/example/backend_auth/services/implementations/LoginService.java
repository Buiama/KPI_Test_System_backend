package com.example.backend_auth.services.implementations;

import com.example.backend_auth.DTOs.requests.LoginRequest;
import com.example.backend_auth.DTOs.responses.LoginResponse;
import com.example.backend_auth.exceptions.StudentNotFoundException;
import com.example.backend_auth.exceptions.WrongPasswordException;
import com.example.backend_auth.models.JwtToken;
import com.example.backend_auth.models.Student;
import com.example.backend_auth.repositories.IJwtTokenRepository;
import com.example.backend_auth.repositories.IStudentRepository;
import com.example.backend_auth.services.interfaces.IJwtService;
import com.example.backend_auth.services.interfaces.ILoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginService implements ILoginService {
    private final IJwtService jwtService;
    private final IStudentRepository studentRepository;
    private final IJwtTokenRepository jwtTokenRepository;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new WrongPasswordException("Wrong password or email");
        }

        Optional<Student> student = studentRepository.findByEmail(request.getEmail());
        if(student.isEmpty()) {
            throw new StudentNotFoundException(request.getEmail());
        }

        String jwtToken = jwtService.getJwtToken(student.get());
        String refreshJwtToken = jwtService.generateRefreshJwtToken(student.get());
        revokeAllStudentJwtTokens(student.get());
        saveStudentJwtToken(student.get(), jwtToken);
        return LoginResponse.builder().accessJwtToken(jwtToken).refreshJwtToken(refreshJwtToken).build();
    }
    private void revokeAllStudentJwtTokens(Student student) {
        List<JwtToken> validUserTokens = jwtTokenRepository.findAllValidJwtTokensByUser(student.getStudentId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtTokenRepository.saveAll(validUserTokens);
    }
    private void saveStudentJwtToken(Student student, String jwtToken) {
        JwtToken jwt = new JwtToken(jwtToken, student);
        jwtTokenRepository.save(jwt);
    }
    public void refreshJwtToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        final String refreshJwtToken = authHeader.substring(7);
        final String email = jwtService.getEmail(refreshJwtToken);
        if (email != null) {
            Student student = this.studentRepository.findByEmail(email).orElseThrow();
            if (jwtService.isJwtTokenValid(refreshJwtToken, student)) {
                String accessToken = jwtService.getJwtToken(student);
                revokeAllStudentJwtTokens(student);
                saveStudentJwtToken(student, accessToken);
                LoginResponse authResponse = LoginResponse.builder()
                        .accessJwtToken(accessToken).refreshJwtToken(refreshJwtToken).build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
