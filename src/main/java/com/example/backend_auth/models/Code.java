package com.example.backend_auth.models;

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
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeId;

    @Column(nullable = false)
    private String language;
    @Column(nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    public Code(String language, String code, Student student) {
        this.language = language;
        this.code = code;
        this.student = student;
    }
}
