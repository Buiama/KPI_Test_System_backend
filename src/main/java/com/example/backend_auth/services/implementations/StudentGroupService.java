package com.example.backend_auth.services.implementations;

import com.example.backend_auth.DTOs.responses.StudentGroupResponse;
import com.example.backend_auth.repositories.IStudentGroupRepository;
import com.example.backend_auth.services.interfaces.IStudentGroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentGroupService implements IStudentGroupService {
    private final IStudentGroupRepository studentGroupRepository;

    public List<StudentGroupResponse> getAllStudentGroups() {
        return studentGroupRepository.findAll().stream()
                .map(group -> new StudentGroupResponse(group.getGroupId(), group.getCode()))
                .collect(Collectors.toList());
    }
}
