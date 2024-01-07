package com.example.backend_auth.services.interfaces;

import com.example.backend_auth.DTOs.requests.LoginRequest;
import com.example.backend_auth.DTOs.responses.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ILoginService {
    LoginResponse login(LoginRequest request);
    void refreshJwtToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
