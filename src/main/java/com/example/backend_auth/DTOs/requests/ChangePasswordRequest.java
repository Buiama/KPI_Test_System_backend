package com.example.backend_auth.DTOs.requests;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private final String currentPassword;
    private final String newPassword;
    private final String confirmationNewPassword;
    private final String email;
}
