package com.example.backend_auth.services.interfaces;

import com.example.backend_auth.DTOs.responses.CodeResponse;
import com.example.backend_auth.DTOs.responses.ExecutedCodeResponse;
import com.example.backend_auth.models.Code;

import java.util.List;

public interface ICodeService {
    void saveCode(Code code);
    List<CodeResponse> getAllCodes();
    ExecutedCodeResponse executeCode(String language, String code);
}
