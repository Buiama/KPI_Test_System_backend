package com.example.backend_auth.repositories;

import com.example.backend_auth.models.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface IStudentGroupRepository extends JpaRepository<StudentGroup, Long> {}
