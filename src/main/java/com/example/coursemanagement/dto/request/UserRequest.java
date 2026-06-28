package com.example.coursemanagement.dto.request;

import com.example.coursemanagement.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {
    @NotBlank(message = "Họ tên không được rỗng")
    private String fullName;

    @NotBlank(message = "Email không được rỗng")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Số điện thoại không được rỗng")
    private String phone;

    private LocalDate dateOfBirth;

    @NotNull(message = "Role không được rỗng")
    private Role role;

    private boolean enabled = true;
}
