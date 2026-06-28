package com.example.coursemanagement.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeacherResponse {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String specialization;
    private String experience;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
