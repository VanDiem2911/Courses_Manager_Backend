package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.TeacherRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Thành công", teacherService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", teacherService.getById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> search(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", teacherService.search(keyword)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Tạo giảng viên thành công", teacherService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable String id, @Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", teacherService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable String id) {
        teacherService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa giảng viên thành công", null));
    }
}
