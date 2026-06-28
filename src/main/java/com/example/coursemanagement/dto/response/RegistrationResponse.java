package com.example.coursemanagement.dto.response;

import com.example.coursemanagement.model.RegistrationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistrationResponse {
    private String id;
    private String courseId;
    private String courseName;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private RegistrationStatus status;
    private LocalDateTime registeredAt;
    private LocalDateTime canceledAt;
}
