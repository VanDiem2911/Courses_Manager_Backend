package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.dto.request.RegistrationRequest;
import com.example.coursemanagement.dto.response.RegistrationResponse;
import com.example.coursemanagement.exception.BadRequestException;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.exception.UnauthorizedException;
import com.example.coursemanagement.model.*;
import com.example.coursemanagement.repository.*;
import com.example.coursemanagement.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @Override
    public List<RegistrationResponse> getAll() {
        return registrationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RegistrationResponse getById(String id) {
        return toResponse(findById(id));
    }

    @Override
    public List<RegistrationResponse> getByCourse(String courseId) {
        return registrationRepository.findByCourseId(courseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistrationResponse> getMyCourses(String email) {
        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student == null) return List.of();
        return registrationRepository.findByStudentId(student.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistrationResponse> getTeacherCourses(String email) {
        Teacher teacher = teacherRepository
                .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase("", email)
                .stream()
                .filter(t -> t.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (teacher == null) return List.of();

        List<String> courseIds = courseRepository.findByTeacherId(teacher.getId())
                .stream().map(Course::getId).collect(Collectors.toList());

        return registrationRepository.findByCourseIdIn(courseIds).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RegistrationResponse register(RegistrationRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        if (course.getStatus() == CourseStatus.CLOSED) {
            throw new BadRequestException("Khóa học đã đóng, không thể đăng ký");
        }
        if (course.getStatus() == CourseStatus.FULL) {
            throw new BadRequestException("Khóa học đã đầy, không thể đăng ký");
        }

        // Check duplicate registration
        registrationRepository.findByStudentEmailAndCourseIdAndStatus(
                request.getEmail(), request.getCourseId(), RegistrationStatus.REGISTERED)
                .ifPresent(r -> {
                    throw new BadRequestException("Email này đã đăng ký khóa học, không thể đăng ký lại");
                });

        // Get or create student
        Student student = studentRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    Student s = new Student();
                    s.setFullName(request.getFullName());
                    s.setEmail(request.getEmail());
                    s.setPhone(request.getPhone());
                    s.setDateOfBirth(request.getDateOfBirth());
                    s.setAddress(request.getAddress());
                    s.setCreatedAt(LocalDateTime.now());
                    s.setUpdatedAt(LocalDateTime.now());
                    return studentRepository.save(s);
                });

        // Create registration
        Registration registration = new Registration();
        registration.setCourseId(course.getId());
        registration.setCourseName(course.getName());
        registration.setStudentId(student.getId());
        registration.setStudentName(student.getFullName());
        registration.setStudentEmail(student.getEmail());
        registration.setStudentPhone(student.getPhone());
        registration.setStatus(RegistrationStatus.REGISTERED);
        registration.setRegisteredAt(LocalDateTime.now());
        registrationRepository.save(registration);

        // Update course count
        course.setRegisteredCount(course.getRegisteredCount() + 1);
        if (course.getRegisteredCount() >= course.getMaxStudents()) {
            course.setStatus(CourseStatus.FULL);
        }
        courseRepository.save(course);

        return toResponse(registration);
    }

    @Override
    public RegistrationResponse cancel(String id, String currentUserEmail) {
        Registration registration = findById(id);

        if (registration.getStatus() == RegistrationStatus.CANCELED) {
            throw new BadRequestException("Đăng ký này đã được hủy trước đó");
        }

        // Check permission: STUDENT can only cancel their own registration
        User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);
        if (currentUser != null && currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByEmail(currentUserEmail).orElse(null);
            if (student == null || !student.getId().equals(registration.getStudentId())) {
                throw new UnauthorizedException("Bạn không có quyền hủy đăng ký này");
            }
        }

        registration.setStatus(RegistrationStatus.CANCELED);
        registration.setCanceledAt(LocalDateTime.now());
        registrationRepository.save(registration);

        // Update course count
        Course course = courseRepository.findById(registration.getCourseId()).orElse(null);
        if (course != null) {
            int newCount = Math.max(0, course.getRegisteredCount() - 1);
            course.setRegisteredCount(newCount);
            if (course.getStatus() == CourseStatus.FULL && newCount < course.getMaxStudents()) {
                course.setStatus(CourseStatus.OPEN);
            }
            courseRepository.save(course);
        }

        return toResponse(registration);
    }

    private Registration findById(String id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đăng ký"));
    }

    private RegistrationResponse toResponse(Registration r) {
        RegistrationResponse res = new RegistrationResponse();
        res.setId(r.getId());
        res.setCourseId(r.getCourseId());
        res.setCourseName(r.getCourseName());
        res.setStudentId(r.getStudentId());
        res.setStudentName(r.getStudentName());
        res.setStudentEmail(r.getStudentEmail());
        res.setStudentPhone(r.getStudentPhone());
        res.setStatus(r.getStatus());
        res.setRegisteredAt(r.getRegisteredAt());
        res.setCanceledAt(r.getCanceledAt());
        return res;
    }
}
