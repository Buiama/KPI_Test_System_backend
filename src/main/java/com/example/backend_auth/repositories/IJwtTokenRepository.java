package com.example.backend_auth.repositories;

import com.example.backend_auth.models.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IJwtTokenRepository extends JpaRepository<JwtToken, Integer> {
    Optional<JwtToken> findByJwtToken(String jwtToken);

    @Query(value = "SELECT jt FROM JwtToken jt, Student s WHERE jt.student.studentId = s.studentId " +
            "AND s.studentId = ?1 AND (jt.isExpired = FALSE OR jt.isRevoked = FALSE)")
    List<JwtToken> findAllValidJwtTokensByUser(Long studentId);
}
