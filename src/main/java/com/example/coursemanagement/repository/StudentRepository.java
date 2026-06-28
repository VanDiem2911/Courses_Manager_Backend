package com.example.coursemanagement.repository;

import com.example.coursemanagement.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByEmail(String email);
    List<Student> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(
            String name, String email, String phone);
}
