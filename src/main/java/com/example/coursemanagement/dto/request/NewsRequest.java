package com.example.coursemanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewsRequest {
    @NotBlank(message = "Tiêu đề tin tức không được rỗng")
    private String title;

    @NotBlank(message = "Tóm tắt không được rỗng")
    private String summary;

    @NotBlank(message = "Nội dung tin tức không được rỗng")
    private String content;

    private String author;
}
