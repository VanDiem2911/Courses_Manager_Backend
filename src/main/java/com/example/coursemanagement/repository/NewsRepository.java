package com.example.coursemanagement.repository;

import com.example.coursemanagement.model.News;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewsRepository extends MongoRepository<News, String> {
    List<News> findAllByOrderByCreatedAtDesc();
}
