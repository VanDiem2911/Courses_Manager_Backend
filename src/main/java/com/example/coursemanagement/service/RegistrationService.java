package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.RegistrationRequest;
import com.example.coursemanagement.dto.response.RegistrationResponse;

import java.util.List;

public interface RegistrationService {
    List<RegistrationResponse> getAll();
    RegistrationResponse getById(String id);
    List<RegistrationResponse> getByCourse(String courseId);
    List<RegistrationResponse> getMyCourses(String email);
    List<RegistrationResponse> getTeacherCourses(String email);
    RegistrationResponse register(RegistrationRequest request, String currentUserEmail);
    RegistrationResponse cancel(String id, String currentUserEmail);
}
