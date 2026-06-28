package com.example.coursemanagement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "students")
public class Student {
    @Id
    private String id;

    private String fullName;

    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private String address;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
