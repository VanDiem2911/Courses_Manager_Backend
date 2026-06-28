package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.NewsRequest;
import com.example.coursemanagement.dto.response.NewsResponse;

import java.util.List;

public interface NewsService {
    List<NewsResponse> getAll();
    NewsResponse getById(String id);
    NewsResponse create(NewsRequest request);
    NewsResponse update(String id, NewsRequest request);
    void delete(String id);
}
