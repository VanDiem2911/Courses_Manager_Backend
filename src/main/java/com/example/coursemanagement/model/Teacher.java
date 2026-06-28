package com.example.coursemanagement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "teachers")
public class Teacher {
    @Id
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
