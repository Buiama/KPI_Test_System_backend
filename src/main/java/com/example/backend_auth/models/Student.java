package com.example.backend_auth.models;

import com.example.backend_auth.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;
import java.util.Collections;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private boolean isEnabled = false;
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.student;
    private boolean isLocked = false;

    @ManyToOne
    @JoinColumn(nullable = false)
    private StudentGroup groupName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "student", cascade = CascadeType.ALL)
    private List<ConfirmationToken> confirmationTokens;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private List<Code> codes;

    public Student(String firstName, String lastName, String email, String password, StudentGroup groupName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.groupName = groupName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isConfirmationTokenExpired() {
        return confirmationTokens.get(confirmationTokens.size()-1).getExpiresAt().isAfter(LocalDateTime.now());
//        return confirmationTokens.stream().anyMatch(token -> token.getExpiresAt().isAfter(LocalDateTime.now()));
    }
}
