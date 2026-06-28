package com.example.coursemanagement.repository;

import com.example.coursemanagement.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContactRepository extends MongoRepository<Contact, String> {
    List<Contact> findAllByOrderByCreatedAtDesc();
}
