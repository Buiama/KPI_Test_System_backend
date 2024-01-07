package com.example.backend_auth.exceptions;

public class WrongPasswordException extends IllegalStateException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
