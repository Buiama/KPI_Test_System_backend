package com.example.backend_auth.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Column(nullable = false)
    private String confirmationToken;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    public ConfirmationToken(String confirmationToken, LocalDateTime createdAt,
                             LocalDateTime expiresAt, Student student) {
        this.confirmationToken = confirmationToken;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.student = student;
    }
}
