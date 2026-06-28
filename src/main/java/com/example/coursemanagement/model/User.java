package com.example.coursemanagement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String fullName;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String phone;

    private LocalDate dateOfBirth;

    private Role role;

    private boolean enabled = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
