package com.example.backend_auth.services.interfaces;

import com.example.backend_auth.models.ConfirmationToken;

import java.util.Optional;

public interface IConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken confirmationToken);
    Optional<ConfirmationToken> getConfirmationToken(String confirmationToken);
    void setConfirmedAt(String confirmationToken);
}
