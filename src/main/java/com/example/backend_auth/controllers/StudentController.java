package com.example.backend_auth.controllers;

import com.example.backend_auth.DTOs.requests.ChangePasswordRequest;
import com.example.backend_auth.DTOs.requests.RunCodeRequest;
import com.example.backend_auth.DTOs.requests.SaveCodeRequest;
import com.example.backend_auth.DTOs.responses.CodeResponse;
import com.example.backend_auth.DTOs.responses.ExecutedCodeResponse;
import com.example.backend_auth.services.implementations.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        studentService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> saveCode(@RequestBody SaveCodeRequest request) {
        studentService.saveCode(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CodeResponse>> getAllCodes() {
        return new ResponseEntity<>(studentService.getAllCodes(), HttpStatus.OK);
    }

    @PostMapping(path = "/run")
    public ResponseEntity<ExecutedCodeResponse> runCode(@RequestBody RunCodeRequest request) {
        return new ResponseEntity<>(studentService.runCode(request), HttpStatus.OK);
    }
}
