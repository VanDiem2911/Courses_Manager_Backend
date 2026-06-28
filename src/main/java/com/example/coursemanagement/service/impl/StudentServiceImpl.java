package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.dto.request.StudentRequest;
import com.example.coursemanagement.dto.response.StudentResponse;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.model.Student;
import com.example.coursemanagement.repository.StudentRepository;
import com.example.coursemanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getById(String id) {
        return toResponse(findById(id));
    }

    @Override
    public StudentResponse create(StudentRequest request) {
        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        return toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse update(String id, StudentRequest request) {
        Student student = findById(id);
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());
        student.setUpdatedAt(LocalDateTime.now());
        return toResponse(studentRepository.save(student));
    }

    @Override
    public void delete(String id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy học viên");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<StudentResponse> search(String keyword) {
        if (!StringUtils.hasText(keyword)) return getAll();
        return studentRepository
                .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(keyword, keyword, keyword)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Student findById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học viên"));
    }

    private StudentResponse toResponse(Student student) {
        StudentResponse r = new StudentResponse();
        r.setId(student.getId());
        r.setFullName(student.getFullName());
        r.setEmail(student.getEmail());
        r.setPhone(student.getPhone());
        r.setDateOfBirth(student.getDateOfBirth());
        r.setAddress(student.getAddress());
        r.setCreatedAt(student.getCreatedAt());
        r.setUpdatedAt(student.getUpdatedAt());
        return r;
    }
}
