package com.example.backend_auth.DTOs.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private final String email;
    private final String password;
}
