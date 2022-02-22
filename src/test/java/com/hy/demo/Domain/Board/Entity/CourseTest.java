package com.hy.demo.Domain.Board.Entity;

import com.hy.demo.Domain.Board.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setup(){


        User manager1 = User.builder()
                .username("manager")
                .role("ROLE_MANAGER")
                .email("manager@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();
        User manager2 = User.builder()
                .username("manager")
                .role("ROLE_MANAGER")
                .email("manager@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();

        Course course1 = Course.builder()
                .courseName("test")
                .user(manager1)
                .heart(0)
                .build();

        Course course2 = Course.builder()
                .courseName("test")
                .user(manager1)
                .heart(0)
                .build();

        Course course3 = Course.builder()
                .courseName("test")
                .user(manager2)
                .heart(0)
                .build();

        userRepository.save(manager1);
        userRepository.save(manager2);

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

    }

    @Test
    public void selectCourse() {

        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<Course> findCourse = courseRepository.findByCourseNameAndUser("test", pageRequest);
        assertThat(findCourse.getSize()).isEqualTo(3);
        System.out.println("findCourse.getContent() = " + findCourse.getContent());


    }


}