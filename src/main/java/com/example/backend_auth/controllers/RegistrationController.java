package com.example.backend_auth.controllers;

import com.example.backend_auth.DTOs.requests.RegistrationRequest;
import com.example.backend_auth.services.implementations.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        return new ResponseEntity<>(registrationService.register(request), HttpStatus.OK);
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return new ResponseEntity<>(registrationService.confirmToken(token), HttpStatus.OK);
    }
}
