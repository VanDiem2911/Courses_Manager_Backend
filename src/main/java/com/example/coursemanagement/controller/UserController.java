package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.UserRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Thành công", userService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", userService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Tạo người dùng thành công", userService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable String id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", userService.update(id, request)));
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<?>> toggleStatus(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái thành công", userService.toggleStatus(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa người dùng thành công", null));
    }
}
