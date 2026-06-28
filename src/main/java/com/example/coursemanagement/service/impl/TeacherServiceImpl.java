package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.dto.request.TeacherRequest;
import com.example.coursemanagement.dto.response.TeacherResponse;
import com.example.coursemanagement.exception.BadRequestException;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.model.Role;
import com.example.coursemanagement.model.Teacher;
import com.example.coursemanagement.model.User;
import com.example.coursemanagement.repository.TeacherRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<TeacherResponse> getAll() {
        return teacherRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherResponse getById(String id) {
        return toResponse(findById(id));
    }

    @Override
    public TeacherResponse create(TeacherRequest request) {
        // Kiểm tra xem email đã được dùng để tạo User chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email đã được sử dụng cho một tài khoản khác");
        }

        // 1. Lưu bản ghi Teacher
        Teacher teacher = new Teacher();
        teacher.setFullName(request.getFullName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
        teacher.setSpecialization(request.getSpecialization());
        teacher.setExperience(request.getExperience());
        teacher.setDescription(request.getDescription());
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
        Teacher savedTeacher = teacherRepository.save(teacher);

        // 2. Tạo tài khoản User tương ứng với role TEACHER
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(Role.TEACHER);
        user.setPassword(passwordEncoder.encode("123456")); // Mật khẩu mặc định
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return toResponse(savedTeacher);
    }

    @Override
    public TeacherResponse update(String id, TeacherRequest request) {
        Teacher teacher = findById(id);
        String oldEmail = teacher.getEmail();

        // Kiểm tra trùng email nếu thay đổi email
        if (!oldEmail.equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email mới đã được sử dụng bởi tài khoản khác");
        }

        // 1. Cập nhật bản ghi Teacher
        teacher.setFullName(request.getFullName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
        teacher.setSpecialization(request.getSpecialization());
        teacher.setExperience(request.getExperience());
        teacher.setDescription(request.getDescription());
        teacher.setUpdatedAt(LocalDateTime.now());
        Teacher savedTeacher = teacherRepository.save(teacher);

        // 2. Cập nhật tài khoản User tương ứng
        userRepository.findByEmail(oldEmail).ifPresent(user -> {
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        });

        return toResponse(savedTeacher);
    }

    @Override
    public void delete(String id) {
        Teacher teacher = findById(id);
        
        // 1. Xóa tài khoản User tương ứng
        userRepository.findByEmail(teacher.getEmail()).ifPresent(user -> {
            userRepository.delete(user);
        });

        // 2. Xóa Teacher
        teacherRepository.delete(teacher);
    }

    @Override
    public List<TeacherResponse> search(String keyword) {
        if (!StringUtils.hasText(keyword)) return getAll();
        return teacherRepository
                .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Teacher findById(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));
    }

    private TeacherResponse toResponse(Teacher teacher) {
        TeacherResponse r = new TeacherResponse();
        r.setId(teacher.getId());
        r.setFullName(teacher.getFullName());
        r.setEmail(teacher.getEmail());
        r.setPhone(teacher.getPhone());
        r.setSpecialization(teacher.getSpecialization());
        r.setExperience(teacher.getExperience());
        r.setDescription(teacher.getDescription());
        r.setCreatedAt(teacher.getCreatedAt());
        r.setUpdatedAt(teacher.getUpdatedAt());
        return r;
    }
}
