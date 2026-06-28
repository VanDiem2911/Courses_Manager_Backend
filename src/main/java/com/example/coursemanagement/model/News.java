package com.example.coursemanagement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "news")
public class News {
    @Id
    private String id;

    private String title;

    private String summary;

    private String content;

    private String author;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
