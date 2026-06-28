package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.dto.request.NewsRequest;
import com.example.coursemanagement.dto.response.NewsResponse;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.model.News;
import com.example.coursemanagement.repository.NewsRepository;
import com.example.coursemanagement.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Override
    public List<NewsResponse> getAll() {
        return newsRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NewsResponse getById(String id) {
        return toResponse(findById(id));
    }

    @Override
    public NewsResponse create(NewsRequest request) {
        News news = new News();
        news.setTitle(request.getTitle());
        news.setSummary(request.getSummary());
        news.setContent(request.getContent());
        news.setImageUrl(request.getImageUrl());
        news.setAuthor(request.getAuthor() != null && !request.getAuthor().isBlank() ? request.getAuthor() : "Ban quản trị");
        news.setCreatedAt(LocalDateTime.now());
        news.setUpdatedAt(LocalDateTime.now());
        return toResponse(newsRepository.save(news));
    }

    @Override
    public NewsResponse update(String id, NewsRequest request) {
        News news = findById(id);
        news.setTitle(request.getTitle());
        news.setSummary(request.getSummary());
        news.setContent(request.getContent());
        news.setImageUrl(request.getImageUrl());
        if (request.getAuthor() != null && !request.getAuthor().isBlank()) {
            news.setAuthor(request.getAuthor());
        }
        news.setUpdatedAt(LocalDateTime.now());
        return toResponse(newsRepository.save(news));
    }

    @Override
    public void delete(String id) {
        News news = findById(id);
        newsRepository.delete(news);
    }

    private News findById(String id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tin tức"));
    }

    private NewsResponse toResponse(News news) {
        NewsResponse r = new NewsResponse();
        r.setId(news.getId());
        r.setTitle(news.getTitle());
        r.setSummary(news.getSummary());
        r.setContent(news.getContent());
        r.setImageUrl(news.getImageUrl());
        r.setAuthor(news.getAuthor());
        r.setCreatedAt(news.getCreatedAt());
        r.setUpdatedAt(news.getUpdatedAt());
        return r;
    }
}
