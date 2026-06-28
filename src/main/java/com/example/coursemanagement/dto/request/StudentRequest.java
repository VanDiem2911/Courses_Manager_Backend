package com.example.coursemanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {
    @NotBlank(message = "Họ tên không được rỗng")
    private String fullName;

    @NotBlank(message = "Email không được rỗng")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại không được rỗng")
    private String phone;

    private LocalDate dateOfBirth;

    private String address;
}
