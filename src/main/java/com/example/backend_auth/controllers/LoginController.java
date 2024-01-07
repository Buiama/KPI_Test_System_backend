package com.example.backend_auth.controllers;

import com.example.backend_auth.DTOs.requests.LoginRequest;
import com.example.backend_auth.DTOs.responses.LoginResponse;
import com.example.backend_auth.services.implementations.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/login")
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return new ResponseEntity<>(loginService.login(request), HttpStatus.OK);
    }

    @PostMapping("/refresh-jwt-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        loginService.refreshJwtToken(request, response);
        return ResponseEntity.ok().build();
    }
}
