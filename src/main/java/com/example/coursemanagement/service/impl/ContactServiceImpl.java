package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.dto.request.ContactRequest;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.model.Contact;
import com.example.coursemanagement.repository.ContactRepository;
import com.example.coursemanagement.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Contact create(ContactRequest request) {
        Contact contact = new Contact();
        contact.setFullName(request.getFullName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setSubject(request.getSubject());
        contact.setMessage(request.getMessage());
        contact.setRead(false);
        contact.setCreatedAt(LocalDateTime.now());
        return contactRepository.save(contact);
    }

    @Override
    public Contact markAsRead(String id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy liên hệ"));
        contact.setRead(true);
        return contactRepository.save(contact);
    }

    @Override
    public void delete(String id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy liên hệ"));
        contactRepository.delete(contact);
    }
}
