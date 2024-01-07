package com.example.backend_auth.services.implementations;

import com.example.backend_auth.DTOs.requests.RegistrationRequest;
import com.example.backend_auth.exceptions.*;
import com.example.backend_auth.models.Student;
import com.example.backend_auth.models.StudentGroup;
import com.example.backend_auth.repositories.IStudentGroupRepository;
import com.example.backend_auth.repositories.IStudentRepository;
import com.example.backend_auth.models.ConfirmationToken;
import com.example.backend_auth.services.interfaces.IConfirmationTokenService;
import com.example.backend_auth.services.interfaces.IEmailService;
import com.example.backend_auth.services.interfaces.IRegistrationService;
import com.example.backend_auth.services.interfaces.IStudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class RegistrationService implements IRegistrationService {
    private final IEmailService emailSender;
    private final IStudentService studentService;
    private final IStudentRepository studentRepository;
    private final Predicate<String> emailValidator;
    private final IStudentGroupRepository studentGroupRepository;
    private final IConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new InvalidEmailException();
        }

        Optional<StudentGroup> group = studentGroupRepository.findById(request.getGroupId());
        if(group.isEmpty()) {
            throw new GroupNotFoundException();
        }

        String confirmationToken;
        Optional<Student> studentInDB = studentRepository.findByEmail(request.getEmail());

        if(studentInDB.isEmpty()) {
            confirmationToken = studentService.signUp(new Student(request.getFirstName(), request.getLastName(),
                    request.getEmail(), request.getPassword(), group.get()));
        }
        else if(studentInDB.get().isEnabled()) {
            throw new EmailAlreadyTakenException();
        }
        else if(studentInDB.get().isConfirmationTokenExpired()) {
            throw new WaitingForConfirmationException();
        }
        else {
            confirmationToken = studentService.resendEmail(studentInDB.get());
        }

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + confirmationToken;
        emailSender.send(request.getEmail(), buildEmail(request.getFirstName() + " " + request.getLastName(),
                link));

        return confirmationToken;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyTakenException();
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        confirmationTokenService.setConfirmedAt(token);
        studentService.enableStudent(confirmationToken.getStudent().getEmail());
        return "Your email has been confirmed";
    }

    private String buildEmail(String name, String link) {
        return """
                <table align="center" border="0" cellpadding="0" cellspacing="0" style=
                "border-collapse:collapse;width:100%;min-width:100%;height:100%" width="100%">
                   <tbody>
                      <tr>
                         <td bgcolor="#FFFFFF" style="padding-top: 20px" valign="top" width="100%">
                            <table align="center" bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" style=
                            "border-collapse:collapse;margin:0 auto" width="580">
                               <tbody>
                                  <tr>
                                     <td align="center" bgcolor="#FFFFFF" style="padding:0" valign="top"><img alt="" border="0" src=
                                     "cid:top" style="display:block" width="55%"></td>
                                  </tr>
                                  <tr>
                                     <td bgcolor="#FFFFFF" style=
                                     "font-size:13px;color:#282828;font-weight:400;text-align:left;font-family:'Open Sans',sans-serif;line-height:24px;vertical-align:top;padding:15px 8px 10px 8px">
                                        <h1 style="font-weight:600;margin:30px 0 50px 0;text-align:center">Welcome to KPI Test System!</h1>
                                        <p>Dear\s
                                        """
                                        +name+
                                        """
                                        ,</p>
                                        <p>You’re just one click away from getting started with KPIonline. All you need to do is
                                        verify your email address to activate your account.</p><a href="
                                        """
                                        +link+
                                        """
                                        " style="padding:10px;width:300px;display:block;text-decoration:none;border:1px solid #306EB0;font-weight:700;font-size:14px;font-family:'Open Sans',sans-serif;color:#fff;background:#306EB0;border-radius:5px;line-height:17px;margin:0 auto;text-align:center"
                                        target="_blank">Confirm My Email</a>
                                        <p>Once your account is activated, you can start using all of KPIonline's features.</p>
                                        <p>You’re receiving this email because you recently created a new KPIonline account. If this
                                        wasn’t you, please ignore this email.</p>
                                        <p>Kind regards,<br>
                                        your KPIonline team</p>
                                     </td>
                                  </tr>
                                  <tr>
                                     <td align="center" bgcolor="#FFFFFF" style="padding:0" valign="bottom"><img alt="" border="0"
                                     src="cid:bottom" style="display:block" width="100%"></td>
                                  </tr>
                               </tbody>
                            </table>
                         </td>
                      </tr>
                   </tbody>
                </table>
                """;
    }
}
