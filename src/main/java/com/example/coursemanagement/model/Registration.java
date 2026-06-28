package com.example.coursemanagement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "registrations")
public class Registration {
    @Id
    private String id;

    private String courseId;

    private String courseName;

    private String studentId;

    private String studentName;

    private String studentEmail;

    private String studentPhone;

    private RegistrationStatus status = RegistrationStatus.REGISTERED;

    private LocalDateTime registeredAt;

    private LocalDateTime canceledAt;
}
