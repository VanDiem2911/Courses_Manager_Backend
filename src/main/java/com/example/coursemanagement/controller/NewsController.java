package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.NewsRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Thành công", newsService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Thành công", newsService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody NewsRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Đăng tin tức thành công", newsService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable String id, @Valid @RequestBody NewsRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật tin tức thành công", newsService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable String id) {
        newsService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa tin tức thành công", null));
    }
}
