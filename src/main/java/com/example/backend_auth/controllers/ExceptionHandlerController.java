package com.example.backend_auth.controllers;

import com.example.backend_auth.DTOs.responses.ErrorMessageResponse;
import com.example.backend_auth.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<ErrorMessageResponse> emailAlreadyTakenException(EmailAlreadyTakenException exception) {
        return new ResponseEntity<>(ErrorMessageResponse.builder().errorMessage(exception.getMessage()).build(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FailedToSendEmailException.class)
    public ResponseEntity<ErrorMessageResponse> failedToSendEmailException(FailedToSendEmailException exception) {
        return new ResponseEntity<>(ErrorMessageResponse.builder().errorMessage(exception.getMessage()).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> GroupNotFound(GroupNotFoundException exception) {
        return new ResponseEntity<>(ErrorMessageResponse.builder().errorMessage(exception.getMessage()).build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorMessageResponse> InvalidEmailException(InvalidEmailException exception) {
        return new ResponseEntity<>(ErrorMessageResponse.builder().errorMessage(exception.getMessage()).build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> StudentNotFoundException(StudentNotFoundException exception) {
        return new ResponseEntity<>(ErrorMessageResponse.builder().errorMessage(exception.getMessage()).build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorMessageResponse> TokenExpired(TokenExpiredException exception) {
        return new ResponseEntity<>(ErrorMessageResponse.builder().errorMessage(exception.getMessage()).build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WaitingForConfirmationException.class)
    public ResponseEntity<ErrorMessageResponse> WaitingForConfirmation(WaitingForConfirmationException exception) {
        return new ResponseEntity<>(ErrorMessageResponse.builder().errorMessage(exception.getMessage()).build(),
                HttpStatus.TEMPORARY_REDIRECT);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorMessageResponse> WrongPasswordException(WrongPasswordException exception) {
        return new ResponseEntity<>(ErrorMessageResponse.builder().errorMessage(exception.getMessage()).build(),
                HttpStatus.UNAUTHORIZED);
    }
}
