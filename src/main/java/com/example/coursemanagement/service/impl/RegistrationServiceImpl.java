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
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private final MongoTemplate mongoTemplate;

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
    public synchronized RegistrationResponse register(RegistrationRequest request, String currentUserEmail) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (course.getStatus() == CourseStatus.CLOSED) {
            throw new BadRequestException("Khóa học đã đóng, không thể đăng ký");
        }
        if (course.getStatus() == CourseStatus.FULL) {
            throw new BadRequestException("Khóa học đã đầy, không thể đăng ký");
        }
        if (course.getRegisteredCount() >= course.getMaxStudents()) {
            mongoTemplate.updateFirst(
                    new Query(Criteria.where("_id").is(course.getId()).and("status").is(CourseStatus.OPEN)),
                    new Update().set("status", CourseStatus.FULL).set("updatedAt", LocalDateTime.now()),
                    Course.class);
            throw new BadRequestException("Khóa học đã đầy, không thể đăng ký");
        }

        // Check duplicate registration
        registrationRepository.findByStudentEmailAndCourseIdAndStatus(
                currentUserEmail, request.getCourseId(), RegistrationStatus.REGISTERED)
                .ifPresent(r -> {
                    throw new BadRequestException("Email này đã đăng ký khóa học, không thể đăng ký lại");
                });

        // Get or create student
        Student student = studentRepository.findByEmail(currentUserEmail)
                .orElseGet(() -> {
                    Student s = new Student();
                    s.setFullName(currentUser.getFullName());
                    s.setEmail(currentUser.getEmail());
                    s.setPhone(currentUser.getPhone());
                    s.setDateOfBirth(request.getDateOfBirth() != null ? request.getDateOfBirth() : currentUser.getDateOfBirth());
                    s.setAddress(request.getAddress());
                    s.setCreatedAt(LocalDateTime.now());
                    s.setUpdatedAt(LocalDateTime.now());
                    return studentRepository.save(s);
                });
        student.setFullName(currentUser.getFullName());
        student.setEmail(currentUser.getEmail());
        student.setPhone(currentUser.getPhone());
        student.setDateOfBirth(request.getDateOfBirth() != null ? request.getDateOfBirth() : currentUser.getDateOfBirth());
        student.setAddress(request.getAddress());
        student.setUpdatedAt(LocalDateTime.now());
        student = studentRepository.save(student);

        LocalDateTime now = LocalDateTime.now();
        Query reserveSeatQuery = new Query(Criteria.where("_id").is(course.getId())
                .and("status").is(CourseStatus.OPEN)
                .and("registeredCount").lt(course.getMaxStudents()));
        Update reserveSeatUpdate = new Update()
                .inc("registeredCount", 1)
                .set("updatedAt", now);
        Course reservedCourse = mongoTemplate.findAndModify(
                reserveSeatQuery,
                reserveSeatUpdate,
                FindAndModifyOptions.options().returnNew(true),
                Course.class);

        if (reservedCourse == null) {
            courseRepository.findById(course.getId()).ifPresent(latestCourse -> {
                if (latestCourse.getStatus() == CourseStatus.OPEN
                        && latestCourse.getRegisteredCount() >= latestCourse.getMaxStudents()) {
                    mongoTemplate.updateFirst(
                            new Query(Criteria.where("_id").is(latestCourse.getId()).and("status").is(CourseStatus.OPEN)),
                            new Update().set("status", CourseStatus.FULL).set("updatedAt", now),
                            Course.class);
                }
            });
            throw new BadRequestException("Khóa học đã đầy, không thể đăng ký");
        }

        if (reservedCourse.getRegisteredCount() >= reservedCourse.getMaxStudents()) {
            mongoTemplate.updateFirst(
                    new Query(Criteria.where("_id").is(reservedCourse.getId()).and("status").is(CourseStatus.OPEN)),
                    new Update().set("status", CourseStatus.FULL).set("updatedAt", now),
                    Course.class);
        }

        // Create registration
        Registration registration = new Registration();
        registration.setCourseId(reservedCourse.getId());
        registration.setCourseName(reservedCourse.getName());
        registration.setStudentId(student.getId());
        registration.setStudentName(student.getFullName());
        registration.setStudentEmail(student.getEmail());
        registration.setStudentPhone(student.getPhone());
        registration.setStatus(RegistrationStatus.REGISTERED);
        registration.setRegisteredAt(now);
        registrationRepository.save(registration);

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

        Query releaseSeatQuery = new Query(Criteria.where("_id").is(registration.getCourseId())
                .and("registeredCount").gt(0));
        Update releaseSeatUpdate = new Update()
                .inc("registeredCount", -1)
                .set("updatedAt", LocalDateTime.now());
        Course updatedCourse = mongoTemplate.findAndModify(
                releaseSeatQuery,
                releaseSeatUpdate,
                FindAndModifyOptions.options().returnNew(true),
                Course.class);
        if (updatedCourse != null
                && updatedCourse.getStatus() == CourseStatus.FULL
                && updatedCourse.getRegisteredCount() < updatedCourse.getMaxStudents()) {
            mongoTemplate.updateFirst(
                    new Query(Criteria.where("_id").is(updatedCourse.getId()).and("status").is(CourseStatus.FULL)),
                    new Update().set("status", CourseStatus.OPEN).set("updatedAt", LocalDateTime.now()),
                    Course.class);
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
