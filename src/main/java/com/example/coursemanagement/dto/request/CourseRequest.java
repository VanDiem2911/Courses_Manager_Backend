package com.example.coursemanagement.dto.request;

import com.example.coursemanagement.model.CourseStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CourseRequest {
    @NotBlank(message = "Tên khóa học không được rỗng")
    private String name;

    private String description;

    private String teacherId;

    private String schedule;

    private String location;

    private List<String> images;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Học phí không được rỗng")
    @DecimalMin(value = "0", message = "Học phí phải >= 0")
    private BigDecimal tuitionFee;

    @Min(value = 1, message = "Số lượng học viên tối đa phải > 0")
    private int maxStudents;

    private CourseStatus status;
}
