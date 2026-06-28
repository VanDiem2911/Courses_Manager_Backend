package com.example.coursemanagement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "courses")
public class Course {
    @Id
    private String id;

    private String name;

    private String description;

    private String teacherId;

    private String teacherName;

    private String schedule;

    private List<String> images;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal tuitionFee;

    private int maxStudents;

    private int registeredCount = 0;

    private CourseStatus status = CourseStatus.OPEN;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
