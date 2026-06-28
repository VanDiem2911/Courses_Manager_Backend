package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.ContactRequest;
import com.example.coursemanagement.model.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> getAll();
    Contact create(ContactRequest request);
    Contact markAsRead(String id);
    void delete(String id);
}
