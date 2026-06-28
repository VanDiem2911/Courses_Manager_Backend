package com.example.coursemanagement.dto.response;

import com.example.coursemanagement.model.Role;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Role role;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
