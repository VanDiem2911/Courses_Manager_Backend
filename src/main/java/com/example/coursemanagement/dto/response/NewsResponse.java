package com.example.coursemanagement.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsResponse {
    private String id;
    private String title;
    private String summary;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
