package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.ContactRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody ContactRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Gửi liên hệ thành công. Chúng tôi sẽ phản hồi sớm nhất!", contactService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Thành công", contactService.getAll()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<?>> markAsRead(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Đã đánh dấu đã đọc", contactService.markAsRead(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable String id) {
        contactService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa liên hệ thành công", null));
    }
}
