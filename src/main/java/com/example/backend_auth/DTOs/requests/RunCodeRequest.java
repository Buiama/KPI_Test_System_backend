package com.example.backend_auth.DTOs.requests;

import lombok.Data;

@Data
public class RunCodeRequest {
    private final String language;
    private final String code;
}
