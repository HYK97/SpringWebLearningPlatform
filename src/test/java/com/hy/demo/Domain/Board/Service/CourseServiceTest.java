package com.hy.demo.Domain.Board.Service;
import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import com.hy.demo.Domain.Board.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Board.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;
import static org.assertj.core.api.AssertionsForClassTypes.entry;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;

    Course course1;
    @BeforeEach
    public void setup(){

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
                .courseName("CourseTest1s")
                .user(manager1)
                .teachName("manager1")
                .courseExplanation("text")
                .build();

        Course course2 = Course.builder()
                .courseName("test2")
                .user(manager1)
                .teachName("manager1")
                .build();

        Course course3 = Course.builder()
                .courseName("test3")
                .user(manager2)
                .teachName("manager2")
                .build();
        Course course4 = Course.builder()
                .courseName("english")
                .user(manager2)
                .teachName("manager2")
                .build();

        CourseEvaluation courseEvaluation1 = CourseEvaluation.builder()
                .user(manager1)
                .scope(3.5)
                .comments("test")
                .course(course1)
                .build();
        CourseEvaluation courseEvaluation2 = CourseEvaluation.builder()
                .user(manager1)
                .scope(4.0)
                .comments("test")
                .course(course1)
                .build();
        CourseEvaluation courseEvaluation3 = CourseEvaluation.builder()
                .user(manager1)
                .scope(4.5)
                .comments("test")
                .course(course1)
                .build();

        userRepository.save(manager1);
        userRepository.save(manager2);

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        courseRepository.save(course4);

        courseEvaluationRepository.save(courseEvaluation1);
        courseEvaluationRepository.save(courseEvaluation2);
        courseEvaluationRepository.save(courseEvaluation3);

    }
    @AfterEach
    public void after(){
        userRepository.deleteAll();
        courseRepository.deleteAll();
        courseEvaluationRepository.deleteAll();
    }
    @Test
    public void detailView() {
        Course byCourseName = courseRepository.findByCourseName(course1.getCourseName());
        CourseDto courseDto = courseService.detailView(byCourseName.getId());
        List<Double> doubles=new ArrayList<>();
        doubles.add(0.0);
        doubles.add(0.0);
        doubles.add(16.666666666666664);
        doubles.add(66.66666666666666);
        doubles.add(16.666666666666664);
        assertThat(courseDto)
                .extracting("courseName","teachName","scope","starScope","courseExplanation","starPercent")
                .containsExactly("CourseTest1s","manager1",4.0,81.5,"text",doubles);

    }

}