package com.example.coursemanagement.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentResponse {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
