package com.example.backend_auth.services.interfaces;

import com.example.backend_auth.DTOs.requests.RegistrationRequest;

public interface IRegistrationService {
    String register(RegistrationRequest request);
    String confirmToken(String token);
}
