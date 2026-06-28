package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.StudentRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Thành công", studentService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", studentService.getById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> search(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", studentService.search(keyword)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Tạo học viên thành công", studentService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable String id, @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", studentService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable String id) {
        studentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa học viên thành công", null));
    }
}
