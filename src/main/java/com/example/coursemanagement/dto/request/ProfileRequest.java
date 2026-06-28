package com.example.coursemanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileRequest {
    @NotBlank(message = "Họ tên không được rỗng")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được rỗng")
    private String phone;

    private LocalDate dateOfBirth;

    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password; // optional - nếu rỗng thì không đổi
}
