package com.example.backend_auth.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecutedCodeResponse {
    @JsonProperty("exit_code")
    private int exitCode;
    @JsonProperty("executed")
    private String executed;
}
