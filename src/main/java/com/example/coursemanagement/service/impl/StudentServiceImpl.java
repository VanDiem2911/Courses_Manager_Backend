package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.dto.request.StudentRequest;
import com.example.coursemanagement.dto.response.StudentResponse;
import com.example.coursemanagement.exception.BadRequestException;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.model.Registration;
import com.example.coursemanagement.model.Student;
import com.example.coursemanagement.model.User;
import com.example.coursemanagement.repository.RegistrationRepository;
import com.example.coursemanagement.repository.StudentRepository;
import com.example.coursemanagement.repository.UserRepository;
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
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;

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
        String oldEmail = student.getEmail();
        if (!oldEmail.equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email đã được sử dụng bởi tài khoản khác");
        }

        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());
        student.setUpdatedAt(LocalDateTime.now());
        Student savedStudent = studentRepository.save(student);

        List<Registration> registrations = registrationRepository.findByStudentId(savedStudent.getId());
        registrations.forEach(registration -> {
            registration.setStudentName(savedStudent.getFullName());
            registration.setStudentEmail(savedStudent.getEmail());
            registration.setStudentPhone(savedStudent.getPhone());
        });
        registrationRepository.saveAll(registrations);

        userRepository.findByEmail(oldEmail).ifPresent(user -> {
            user.setFullName(savedStudent.getFullName());
            user.setEmail(savedStudent.getEmail());
            user.setPhone(savedStudent.getPhone());
            user.setDateOfBirth(savedStudent.getDateOfBirth());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        });

        return toResponse(savedStudent);
    }

    @Override
    public void delete(String id) {
        Student student = findById(id);
        if (!registrationRepository.findByStudentId(id).isEmpty()) {
            throw new BadRequestException("Không thể xóa học viên đang có dữ liệu đăng ký khóa học");
        }
        studentRepository.delete(student);
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
        User user = userRepository.findByEmail(student.getEmail()).orElse(null);
        StudentResponse r = new StudentResponse();
        r.setId(student.getId());
        r.setFullName(student.getFullName());
        r.setEmail(student.getEmail());
        r.setPhone(student.getPhone());
        r.setDateOfBirth(student.getDateOfBirth() != null
                ? student.getDateOfBirth()
                : user != null ? user.getDateOfBirth() : null);
        r.setAddress(student.getAddress());
        r.setCreatedAt(student.getCreatedAt());
        r.setUpdatedAt(student.getUpdatedAt());
        return r;
    }
}
