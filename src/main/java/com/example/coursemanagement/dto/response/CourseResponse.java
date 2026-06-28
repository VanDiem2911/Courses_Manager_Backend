package com.example.coursemanagement.dto.response;

import com.example.coursemanagement.model.CourseStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseResponse {
    private String id;
    private String name;
    private String description;
    private String teacherId;
    private String teacherName;
    private String schedule;
    private String location;
    private List<String> images;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal tuitionFee;
    private int maxStudents;
    private int registeredCount;
    private CourseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
