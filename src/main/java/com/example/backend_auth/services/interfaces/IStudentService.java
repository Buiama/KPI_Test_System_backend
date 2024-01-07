package com.example.backend_auth.services.interfaces;

import com.example.backend_auth.DTOs.requests.ChangePasswordRequest;
import com.example.backend_auth.models.Student;

public interface IStudentService {
    String signUp(Student student);
    void changePassword(ChangePasswordRequest request);
//    void saveCode(SaveCodeRequest request);
    void enableStudent(String email);
    String resendEmail(Student student);
}
