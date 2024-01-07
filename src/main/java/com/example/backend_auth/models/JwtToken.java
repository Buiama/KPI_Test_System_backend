package com.example.backend_auth.models;

import com.example.backend_auth.enums.JwtTokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jwtTokenId;

    @Column(unique = true)
    private String jwtToken;
    @Enumerated(EnumType.STRING)
    private JwtTokenType jwtTokenType = JwtTokenType.bearer;
    private boolean isRevoked = false;
    private boolean isExpired = false;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    public JwtToken(String jwtToken, Student student) {
        this.jwtToken = jwtToken;
        this.student = student;
    }
}
