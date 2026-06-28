package com.example.coursemanagement.dto.response;

import com.example.coursemanagement.model.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthResponse {
    private String token;
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Role role;
}
