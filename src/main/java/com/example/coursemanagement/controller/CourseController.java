package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.CourseRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.model.CourseStatus;
import com.example.coursemanagement.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Thành công", courseService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", courseService.getById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String teacherId,
            @RequestParam(required = false) CourseStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", courseService.search(keyword, teacherId, status)));
    }

    @GetMapping("/my-teaching-courses")
    public ResponseEntity<ApiResponse<?>> myTeachingCourses(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", courseService.getMyTeachingCourses(userDetails.getUsername())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Tạo khóa học thành công", courseService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable String id,
                                                  @Valid @RequestBody CourseRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật khóa học thành công",
                courseService.update(id, request, userDetails.getUsername())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable String id) {
        courseService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa khóa học thành công", null));
    }
}
