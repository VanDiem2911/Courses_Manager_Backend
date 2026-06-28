package com.example.coursemanagement.repository;

import com.example.coursemanagement.model.Registration;
import com.example.coursemanagement.model.RegistrationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends MongoRepository<Registration, String> {
    List<Registration> findByCourseId(String courseId);
    List<Registration> findByStudentId(String studentId);
    Optional<Registration> findByStudentEmailAndCourseIdAndStatus(
            String studentEmail, String courseId, RegistrationStatus status);
    boolean existsByCourseId(String courseId);
    List<Registration> findByCourseIdIn(List<String> courseIds);
}
