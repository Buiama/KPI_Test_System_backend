package com.example.backend_auth.repositories;

import com.example.backend_auth.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface IStudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Student SET isEnabled = TRUE WHERE email = ?1")
    void enableStudent(String email);
}
