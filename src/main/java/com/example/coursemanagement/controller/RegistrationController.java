package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.RegistrationRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Thành công", registrationService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", registrationService.getById(id)));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<?>> getByCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", registrationService.getByCourse(courseId)));
    }

    @GetMapping("/my-courses")
    public ResponseEntity<ApiResponse<?>> myCourses(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", registrationService.getMyCourses(userDetails.getUsername())));
    }

    @GetMapping("/teacher-courses")
    public ResponseEntity<ApiResponse<?>> teacherCourses(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", registrationService.getTeacherCourses(userDetails.getUsername())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Đăng ký khóa học thành công", registrationService.register(request)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<?>> cancel(@PathVariable String id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success("Hủy đăng ký thành công",
                registrationService.cancel(id, userDetails.getUsername())));
    }
}
