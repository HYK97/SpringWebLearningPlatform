package com.hy.demo.domain.board.service;

import com.hy.demo.domain.course.dto.CourseDto;
import com.hy.demo.domain.course.entity.Course;
import com.hy.demo.domain.course.entity.CourseEvaluation;
import com.hy.demo.domain.course.repository.CourseEvaluationRepository;
import com.hy.demo.domain.course.repository.CourseRepository;
import com.hy.demo.domain.course.service.CourseService;
import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseServiceTest {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;
    private Course course1;

    private Long courseEvaluation4Id;
    private CourseEvaluation courseEvaluation4;

    @BeforeEach
    public void setup() {

        User user1 = User.builder()
                .username("user1")
                .role("ROLE_USER")
                .email("user@gmail.com")
                .password(passwordEncoder.encode("user"))
                .build();

        User user2 = User.builder()
                .username("user2")
                .role("ROLE_USER")
                .email("user@gmail.com")
                .password(passwordEncoder.encode("user"))
                .build();


        User manager1 = User.builder()
                .username("manager1")
                .role("ROLE_MANAGER")
                .email("manager@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();
        User manager2 = User.builder()
                .username("manager2")
                .role("ROLE_MANAGER")
                .email("manager@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();

        course1 = Course.builder()
                .courseName("courseTest1")
                .user(manager1)
                .build();

        Course course2 = Course.builder()
                .courseName("courseTest2")
                .user(manager1)
                .build();

        Course course3 = Course.builder()
                .courseName("courseTest3")
                .user(manager2)
                .build();
        Course course4 = Course.builder()
                .courseName("courseTest4")
                .user(manager2)
                .build();

        CourseEvaluation courseEvaluation1 = CourseEvaluation.builder()
                .user(user1)
                .scope(3.5)
                .comments("test1")
                .course(course1)
                .build();
        CourseEvaluation courseEvaluation2 = CourseEvaluation.builder()
                .user(user2)
                .scope(4.0)
                .comments("test2")
                .course(course1)
                .build();
        CourseEvaluation courseEvaluation3 = CourseEvaluation.builder()
                .user(manager1)
                .scope(4.5)
                .comments("test3")
                .course(course1)
                .build();
        courseEvaluation4 = CourseEvaluation.builder()
                .user(manager2)
                .scope(1.0)
                .comments("test4")
                .course(course2)
                .build();


        userRepository.save(manager1);
        userRepository.save(manager2);
        userRepository.save(user1);
        userRepository.save(user2);

        courseRepository.save(course3);
        courseRepository.save(course4);

        courseEvaluationRepository.save(courseEvaluation1);
        courseEvaluationRepository.save(courseEvaluation2);
        courseEvaluationRepository.save(courseEvaluation3);
        courseEvaluation4Id = courseEvaluationRepository.save(courseEvaluation4).getId();


        CourseEvaluation reply1 = CourseEvaluation.builder()
                .user(manager1)
                .replyId(courseEvaluation4Id)
                .comments("reply1")
                .course(course2)
                .build();

        courseEvaluationRepository.save(reply1);
    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
        courseEvaluationRepository.deleteAll();
    }

    @Test
    public void detailView() {
        //given
        List<Double> doubles = new ArrayList<>();
        doubles.add(16.666666666666664);
        doubles.add(66.66666666666666);
        doubles.add(16.666666666666664);
        doubles.add(0.0);
        doubles.add(0.0);
        //when
        Course byCourseName = courseRepository.findByCourseName(course1.getCourseName());
        CourseDto courseDto = courseService.findDetailCourse(byCourseName.getId());
        //then
        assertThat(courseDto)
                .extracting("courseName", "user.username", "scope", "starScope", "courseExplanation", "starPercent")
                .containsExactly("courseTest1", "manager1", 4.0, 81.5, null, doubles);

    }

    @Test
    public void randomCourseList() throws Exception {
        //given
        int amount = 2;
        //when
        List<CourseDto> courseDto = courseService.randomCourseList(amount);
        //then
        assertThat(courseDto.size()).isEqualTo(2);

    }


}
    
  
