package com.example.coursemanagement.repository;

import com.example.coursemanagement.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    List<Teacher> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
}
