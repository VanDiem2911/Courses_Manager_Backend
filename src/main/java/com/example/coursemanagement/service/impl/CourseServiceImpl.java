package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.dto.request.CourseRequest;
import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.exception.BadRequestException;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.exception.UnauthorizedException;
import com.example.coursemanagement.model.*;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.RegistrationRepository;
import com.example.coursemanagement.repository.TeacherRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private static final String DEFAULT_LOCATION = "232 Nguyễn Thị Minh Khai";

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;

    @Override
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse getById(String id) {
        return toResponse(findById(id));
    }

    @Override
    public CourseResponse create(CourseRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null
                && request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Ngày bắt đầu không được sau ngày kết thúc");
        }

        Course course = new Course();
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setSchedule(request.getSchedule());
        course.setLocation(resolveLocation(request.getLocation()));
        course.setImages(request.getImages());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());
        course.setTuitionFee(request.getTuitionFee());
        course.setMaxStudents(request.getMaxStudents());
        course.setRegisteredCount(0);
        course.setStatus(CourseStatus.OPEN);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        if (StringUtils.hasText(request.getTeacherId())) {
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));
            course.setTeacherId(teacher.getId());
            course.setTeacherName(teacher.getFullName());
        }

        return toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse update(String id, CourseRequest request, String currentUserEmail) {
        Course course = findById(id);

        // Check teacher permission
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (currentUser.getRole() == Role.TEACHER) {
            // Teacher can only update their own courses
            Teacher teacher = teacherRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    "", currentUserEmail).stream()
                    .filter(t -> t.getEmail().equals(currentUserEmail))
                    .findFirst().orElse(null);

            if (teacher == null || !teacher.getId().equals(course.getTeacherId())) {
                throw new UnauthorizedException("Bạn không có quyền chỉnh sửa khóa học này");
            }
        }

        if (request.getStartDate() != null && request.getEndDate() != null
                && request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Ngày bắt đầu không được sau ngày kết thúc");
        }

        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setSchedule(request.getSchedule());
        course.setLocation(resolveLocation(request.getLocation()));
        course.setImages(request.getImages());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());
        course.setTuitionFee(request.getTuitionFee());
        course.setMaxStudents(request.getMaxStudents());
        course.setUpdatedAt(LocalDateTime.now());

        if (StringUtils.hasText(request.getTeacherId())) {
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));
            course.setTeacherId(teacher.getId());
            course.setTeacherName(teacher.getFullName());
        } else {
            course.setTeacherId(null);
            course.setTeacherName(null);
        }

        // Update status
        if (request.getStatus() != null) {
            course.setStatus(request.getStatus());
        } else {
            updateCourseStatus(course);
        }

        return toResponse(courseRepository.save(course));
    }

    @Override
    public void delete(String id) {
        Course course = findById(id);
        if (registrationRepository.existsByCourseId(id)) {
            throw new BadRequestException("Không thể xóa khóa học đã có học viên đăng ký");
        }
        courseRepository.delete(course);
    }

    @Override
    public List<CourseResponse> search(String keyword, String teacherId, CourseStatus status) {
        List<Course> courses;

        if (StringUtils.hasText(keyword)) {
            courses = courseRepository.findByNameContainingIgnoreCase(keyword);
        } else {
            courses = courseRepository.findAll();
        }

        if (StringUtils.hasText(teacherId)) {
            courses = courses.stream()
                    .filter(c -> teacherId.equals(c.getTeacherId()))
                    .collect(Collectors.toList());
        }

        if (status != null) {
            courses = courses.stream()
                    .filter(c -> c.getStatus() == status)
                    .collect(Collectors.toList());
        }

        return courses.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getMyTeachingCourses(String email) {
        Teacher teacher = teacherRepository
                .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase("", email)
                .stream()
                .filter(t -> t.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (teacher == null) return List.of();

        return courseRepository.findByTeacherId(teacher.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateCourseStatus(Course course) {
        if (course.getStatus() == CourseStatus.CLOSED) return;
        if (course.getRegisteredCount() >= course.getMaxStudents()) {
            course.setStatus(CourseStatus.FULL);
        } else {
            course.setStatus(CourseStatus.OPEN);
        }
    }

    private Course findById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));
    }

    public CourseResponse toResponse(Course course) {
        CourseResponse r = new CourseResponse();
        r.setId(course.getId());
        r.setName(course.getName());
        r.setDescription(course.getDescription());
        r.setTeacherId(course.getTeacherId());
        r.setTeacherName(course.getTeacherName());
        r.setSchedule(course.getSchedule());
        r.setLocation(course.getLocation());
        r.setImages(course.getImages());
        r.setStartDate(course.getStartDate());
        r.setEndDate(course.getEndDate());
        r.setTuitionFee(course.getTuitionFee());
        r.setMaxStudents(course.getMaxStudents());
        r.setRegisteredCount(course.getRegisteredCount());
        r.setStatus(course.getStatus());
        r.setCreatedAt(course.getCreatedAt());
        r.setUpdatedAt(course.getUpdatedAt());
        return r;
    }

    private String resolveLocation(String location) {
        return StringUtils.hasText(location) ? location : DEFAULT_LOCATION;
    }
}
