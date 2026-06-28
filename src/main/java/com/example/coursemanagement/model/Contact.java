package com.example.coursemanagement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "contacts")
public class Contact {
    @Id
    private String id;

    private String fullName;

    private String email;

    private String phone;

    private String subject;

    private String message;

    private boolean read = false;

    private LocalDateTime createdAt;
}
