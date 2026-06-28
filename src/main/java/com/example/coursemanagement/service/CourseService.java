package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.CourseRequest;
import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.model.CourseStatus;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getAll();
    CourseResponse getById(String id);
    CourseResponse create(CourseRequest request);
    CourseResponse update(String id, CourseRequest request, String currentUserEmail);
    void delete(String id);
    List<CourseResponse> search(String keyword, String teacherId, CourseStatus status);
    List<CourseResponse> getMyTeachingCourses(String email);
}
