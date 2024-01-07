package com.example.backend_auth.DTOs.requests;

import lombok.Data;

@Data
public class SaveCodeRequest {
    private final String language;
    private final String code;
    private final String email;
}
