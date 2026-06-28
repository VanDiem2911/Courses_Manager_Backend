package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.StudentRequest;
import com.example.coursemanagement.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {
    List<StudentResponse> getAll();
    StudentResponse getById(String id);
    StudentResponse create(StudentRequest request);
    StudentResponse update(String id, StudentRequest request);
    void delete(String id);
    List<StudentResponse> search(String keyword);
}
