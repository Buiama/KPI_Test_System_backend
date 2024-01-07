package com.example.backend_auth.services.interfaces;

import com.example.backend_auth.DTOs.responses.StudentGroupResponse;

import java.util.List;

public interface IStudentGroupService {
    List<StudentGroupResponse> getAllStudentGroups();
}
