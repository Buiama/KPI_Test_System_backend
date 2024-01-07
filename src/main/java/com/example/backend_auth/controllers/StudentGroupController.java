package com.example.backend_auth.controllers;

import com.example.backend_auth.DTOs.responses.StudentGroupResponse;
import com.example.backend_auth.services.implementations.StudentGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class StudentGroupController {
    private final StudentGroupService studentGroupService;

    @GetMapping
    public ResponseEntity<List<StudentGroupResponse>> getAllStudentGroups() {
        return new ResponseEntity<>(studentGroupService.getAllStudentGroups(), HttpStatus.OK);
    }
}
