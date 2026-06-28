package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.LoginRequest;
import com.example.coursemanagement.dto.request.ProfileRequest;
import com.example.coursemanagement.dto.request.RegisterRequest;
import com.example.coursemanagement.dto.response.AuthResponse;
import com.example.coursemanagement.dto.response.UserResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse getCurrentUser(String email);
    UserResponse updateProfile(String email, ProfileRequest request);
}
