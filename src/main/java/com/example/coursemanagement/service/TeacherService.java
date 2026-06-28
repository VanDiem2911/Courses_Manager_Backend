package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.TeacherRequest;
import com.example.coursemanagement.dto.response.TeacherResponse;

import java.util.List;

public interface TeacherService {
    List<TeacherResponse> getAll();
    TeacherResponse getById(String id);
    TeacherResponse create(TeacherRequest request);
    TeacherResponse update(String id, TeacherRequest request);
    void delete(String id);
    List<TeacherResponse> search(String keyword);
}
