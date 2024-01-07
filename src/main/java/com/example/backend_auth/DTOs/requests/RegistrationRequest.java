package com.example.backend_auth.DTOs.requests;

import lombok.Data;

@Data
public class RegistrationRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final Long groupId;
}
