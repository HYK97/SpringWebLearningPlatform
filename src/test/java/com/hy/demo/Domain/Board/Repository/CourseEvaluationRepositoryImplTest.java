package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
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
import static org.assertj.core.api.AssertionsForClassTypes.entry;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseEvaluationRepositoryImplTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;


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

        Course course1 = Course.builder()
                .courseName("test1")
                .user(manager1)
                .teachName("manager1")
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

    @Test
    public void countScope() {
        Map<String, Double> stringDoubleMap = courseEvaluationRepository.countScope(1L);
        for( String key : stringDoubleMap.keySet() ) {
            System.out.println(String.format("키 : %s, 값 : %s", key, stringDoubleMap.get(key)));
        }
        assertThat(stringDoubleMap).hasSize(5)
                .contains(entry("1",0.0), entry("2",0.0), entry("3",0.5), entry("4",2.0), entry("5",0.5));
    }



}