package com.example.coursemanagement.repository;

import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.model.CourseStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {
    List<Course> findByTeacherId(String teacherId);
    List<Course> findByStatus(CourseStatus status);
    List<Course> findByNameContainingIgnoreCase(String keyword);
}
