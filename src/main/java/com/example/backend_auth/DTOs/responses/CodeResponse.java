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
public class CodeResponse {
    @JsonProperty("id")
    private Long groupId;
    @JsonProperty("language")
    private String language;
    @JsonProperty("code")
    private String code;
    @JsonProperty("email")
    private String email;
}
