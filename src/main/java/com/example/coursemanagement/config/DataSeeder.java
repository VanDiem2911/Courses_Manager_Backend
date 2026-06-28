package com.example.coursemanagement.config;

import com.example.coursemanagement.model.*;
import com.example.coursemanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final NewsRepository newsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<String> defaultImages = List.of(
            "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=600&auto=format&fit=crop&q=60",
            "https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=600&auto=format&fit=crop&q=60",
            "https://images.unsplash.com/photo-1542831371-29b0f74f9713?w=600&auto=format&fit=crop&q=60",
            "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600&auto=format&fit=crop&q=60",
            "https://images.unsplash.com/photo-1581291518633-83b4ebd1d83e?w=600&auto=format&fit=crop&q=60"
        );

        courseRepository.findAll().forEach(course -> {
            if (course.getImages() == null || course.getImages().isEmpty()) {
                course.setImages(defaultImages);
                courseRepository.save(course);
                System.out.println("Set default images for course: " + course.getName());
            }
        });

        if (userRepository.count() > 0) return;

        // Seed users
        User admin = new User();
        admin.setFullName("Administrator");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setPhone("0900000001");
        admin.setDateOfBirth(LocalDate.of(1990, 1, 1));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        userRepository.save(admin);

        User teacher = new User();
        teacher.setFullName("Nguyễn Văn Giáo");
        teacher.setEmail("teacher@gmail.com");
        teacher.setPassword(passwordEncoder.encode("123456"));
        teacher.setPhone("0900000002");
        teacher.setDateOfBirth(LocalDate.of(1985, 5, 15));
        teacher.setRole(Role.TEACHER);
        teacher.setEnabled(true);
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
        userRepository.save(teacher);

        User student = new User();
        student.setFullName("Trần Thị Học");
        student.setEmail("student@gmail.com");
        student.setPassword(passwordEncoder.encode("123456"));
        student.setPhone("0900000003");
        student.setDateOfBirth(LocalDate.of(2000, 10, 20));
        student.setRole(Role.STUDENT);
        student.setEnabled(true);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        userRepository.save(student);

        // Seed teacher record for the main teacher@gmail.com account
        Teacher t0 = new Teacher();
        t0.setFullName("Nguyễn Văn Giáo");
        t0.setEmail("teacher@gmail.com");
        t0.setPhone("0900000002");
        t0.setSpecialization("Cơ sở dữ liệu");
        t0.setExperience("8 năm");
        t0.setDescription("Giảng viên cơ sở dữ liệu và hệ quản trị CSDL.");
        t0.setCreatedAt(LocalDateTime.now());
        t0.setUpdatedAt(LocalDateTime.now());
        teacherRepository.save(t0);

        // Seed teachers
        Teacher t1 = new Teacher();
        t1.setFullName("Nguyễn Văn An");
        t1.setEmail("an.nguyen@hudi.edu.vn");
        t1.setPhone("0911000001");
        t1.setSpecialization("Lập trình Web");
        t1.setExperience("5 năm");
        t1.setDescription("Giảng viên lập trình web với kinh nghiệm thực tế tại nhiều công ty lớn.");
        t1.setCreatedAt(LocalDateTime.now());
        t1.setUpdatedAt(LocalDateTime.now());
        teacherRepository.save(t1);

        User u1 = new User();
        u1.setFullName(t1.getFullName());
        u1.setEmail(t1.getEmail());
        u1.setPassword(passwordEncoder.encode("123456"));
        u1.setPhone(t1.getPhone());
        u1.setDateOfBirth(LocalDate.of(1990, 5, 10));
        u1.setRole(Role.TEACHER);
        u1.setEnabled(true);
        u1.setCreatedAt(LocalDateTime.now());
        u1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(u1);

        Teacher t2 = new Teacher();
        t2.setFullName("Lê Thị Bình");
        t2.setEmail("binh.le@hudi.edu.vn");
        t2.setPhone("0911000002");
        t2.setSpecialization("Khoa học dữ liệu");
        t2.setExperience("7 năm");
        t2.setDescription("Chuyên gia về Machine Learning và Data Science.");
        t2.setCreatedAt(LocalDateTime.now());
        t2.setUpdatedAt(LocalDateTime.now());
        teacherRepository.save(t2);

        User u2 = new User();
        u2.setFullName(t2.getFullName());
        u2.setEmail(t2.getEmail());
        u2.setPassword(passwordEncoder.encode("123456"));
        u2.setPhone(t2.getPhone());
        u2.setDateOfBirth(LocalDate.of(1988, 8, 18));
        u2.setRole(Role.TEACHER);
        u2.setEnabled(true);
        u2.setCreatedAt(LocalDateTime.now());
        u2.setUpdatedAt(LocalDateTime.now());
        userRepository.save(u2);

        Teacher t3 = new Teacher();
        t3.setFullName("Phạm Văn Cường");
        t3.setEmail("cuong.pham@hudi.edu.vn");
        t3.setPhone("0911000003");
        t3.setSpecialization("An ninh mạng");
        t3.setExperience("10 năm");
        t3.setDescription("Chuyên gia bảo mật với nhiều chứng chỉ quốc tế CISSP, CEH.");
        t3.setCreatedAt(LocalDateTime.now());
        t3.setUpdatedAt(LocalDateTime.now());
        teacherRepository.save(t3);

        User u3 = new User();
        u3.setFullName(t3.getFullName());
        u3.setEmail(t3.getEmail());
        u3.setPassword(passwordEncoder.encode("123456"));
        u3.setPhone(t3.getPhone());
        u3.setDateOfBirth(LocalDate.of(1980, 12, 25));
        u3.setRole(Role.TEACHER);
        u3.setEnabled(true);
        u3.setCreatedAt(LocalDateTime.now());
        u3.setUpdatedAt(LocalDateTime.now());
        userRepository.save(u3);

        // Seed courses
        Course c1 = new Course();
        c1.setName("Lập trình React từ cơ bản đến nâng cao");
        c1.setDescription("Khóa học React.js toàn diện từ cơ bản đến nâng cao, bao gồm hooks, context, redux và các dự án thực tế.");
        c1.setTeacherId(t1.getId());
        c1.setTeacherName(t1.getFullName());
        c1.setSchedule("Thứ 2 - 4 - 6, 18:00 - 20:00");
        c1.setStartDate(LocalDate.now().plusDays(10));
        c1.setEndDate(LocalDate.now().plusDays(70));
        c1.setTuitionFee(new BigDecimal("3500000"));
        c1.setMaxStudents(30);
        c1.setRegisteredCount(0);
        c1.setStatus(CourseStatus.OPEN);
        c1.setCreatedAt(LocalDateTime.now());
        c1.setUpdatedAt(LocalDateTime.now());
        c1.setImages(defaultImages);
        courseRepository.save(c1);

        Course c2 = new Course();
        c2.setName("Machine Learning cơ bản với Python");
        c2.setDescription("Học Machine Learning từ đầu với Python, scikit-learn, pandas. Bao gồm các thuật toán phổ biến và dự án thực tế.");
        c2.setTeacherId(t2.getId());
        c2.setTeacherName(t2.getFullName());
        c2.setSchedule("Thứ 3 - 5 - 7, 19:00 - 21:00");
        c2.setStartDate(LocalDate.now().plusDays(15));
        c2.setEndDate(LocalDate.now().plusDays(90));
        c2.setTuitionFee(new BigDecimal("4500000"));
        c2.setMaxStudents(25);
        c2.setRegisteredCount(0);
        c2.setStatus(CourseStatus.OPEN);
        c2.setCreatedAt(LocalDateTime.now());
        c2.setUpdatedAt(LocalDateTime.now());
        c2.setImages(defaultImages);
        courseRepository.save(c2);

        Course c3 = new Course();
        c3.setName("An ninh mạng và bảo mật hệ thống");
        c3.setDescription("Tổng quan về an ninh mạng, các loại tấn công phổ biến, phòng thủ và công cụ bảo mật. Phù hợp cho người mới bắt đầu.");
        c3.setTeacherId(t3.getId());
        c3.setTeacherName(t3.getFullName());
        c3.setSchedule("Thứ 7 - Chủ nhật, 08:00 - 12:00");
        c3.setStartDate(LocalDate.now().plusDays(5));
        c3.setEndDate(LocalDate.now().plusDays(65));
        c3.setTuitionFee(new BigDecimal("5000000"));
        c3.setMaxStudents(20);
        c3.setRegisteredCount(0);
        c3.setStatus(CourseStatus.OPEN);
        c3.setCreatedAt(LocalDateTime.now());
        c3.setUpdatedAt(LocalDateTime.now());
        c3.setImages(defaultImages);
        courseRepository.save(c3);

        Course c4 = new Course();
        c4.setName("Java Spring Boot Backend Development");
        c4.setDescription("Xây dựng REST API với Spring Boot, Spring Security, JPA. Học từ cơ bản đến microservices.");
        c4.setTeacherId(t1.getId());
        c4.setTeacherName(t1.getFullName());
        c4.setSchedule("Thứ 2 - 4, 20:00 - 22:00");
        c4.setStartDate(LocalDate.now().plusDays(20));
        c4.setEndDate(LocalDate.now().plusDays(80));
        c4.setTuitionFee(new BigDecimal("4000000"));
        c4.setMaxStudents(30);
        c4.setRegisteredCount(0);
        c4.setStatus(CourseStatus.OPEN);
        c4.setCreatedAt(LocalDateTime.now());
        c4.setUpdatedAt(LocalDateTime.now());
        c4.setImages(defaultImages);
        courseRepository.save(c4);

        // Seed news
        if (newsRepository.count() == 0) {
            News n1 = new News();
            n1.setTitle("Chào mừng năm học mới 2026 - Cơ hội nhận học bổng 50%");
            n1.setSummary("DUDI software hân hoan chào đón năm học mới 2026 với chương trình khuyến học đặc biệt dành cho các bạn đam mê CNTT.");
            n1.setContent("Nhằm khuyến khích và tạo điều kiện cho các bạn trẻ đam mê công nghệ, DUDI software xin thông báo khởi động chương trình học bổng năm học mới. Các học viên đăng ký sớm trước ngày 15/07/2026 sẽ được giảm trực tiếp 50% học phí cho các khóa học ReactJS và Java Spring Boot. Cơ hội tuyệt vời để nâng cao kỹ năng thực chiến cùng các chuyên gia hàng đầu.");
            n1.setAuthor("Ban tuyển sinh");
            n1.setCreatedAt(LocalDateTime.now().minusDays(2));
            n1.setUpdatedAt(LocalDateTime.now().minusDays(2));
            newsRepository.save(n1);

            News n2 = new News();
            n2.setTitle("Bật mí lộ trình trở thành Fullstack Developer trong 6 tháng");
            n2.setSummary("Tổng hợp lộ trình chi tiết và các kỹ năng cần thiết từ cơ bản đến nâng cao để trở thành Fullstack Developer chuyên nghiệp.");
            n2.setContent("Trong thế giới công nghệ thay đổi không ngừng, lập trình viên Fullstack luôn là vị trí được săn đón hàng đầu. Để giúp các bạn định hình con đường của mình, chúng tôi xin chia sẻ lộ trình 6 tháng học và thực hành: Tháng 1-2 tập trung vào HTML/CSS/JS và nền tảng giao diện; Tháng 3-4 học React.js và các UI framework; Tháng 5-6 làm quen với backend bằng Java Spring Boot và cơ sở dữ liệu MongoDB. Hãy tham gia ngay khóa học tại DUDI software để được hướng dẫn trực tiếp nhé!");
            n2.setAuthor("Ban chuyên môn");
            n2.setCreatedAt(LocalDateTime.now().minusDays(5));
            n2.setUpdatedAt(LocalDateTime.now().minusDays(5));
            newsRepository.save(n2);
        }

        System.out.println("✅ Seed data completed!");
    }
}
