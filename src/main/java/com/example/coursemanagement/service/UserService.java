package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.UserRequest;
import com.example.coursemanagement.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll();
    UserResponse getById(String id);
    UserResponse create(UserRequest request);
    UserResponse update(String id, UserRequest request);
    UserResponse toggleStatus(String id);
    void delete(String id);
}
