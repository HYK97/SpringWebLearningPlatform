package com.hy.demo.Domain.Board.Service;


import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import com.hy.demo.Domain.Course.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.Course.Service.CourseEvaluationService;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseEvaluationServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseEvaluationService courseEvaluationService;

    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    Logger logger;

    private Course course1;

    private Long course1Id;
    private Long course2Id;
    private Long courseEvaluation4Id;
    private Long courseEvaluation1Id;
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

        course1Id = courseRepository.save(course1).getId();
        course2Id = courseRepository.save(course2).getId();
        courseRepository.save(course3);
        courseRepository.save(course4);

        courseEvaluation1Id = courseEvaluationRepository.save(courseEvaluation1).getId();
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
    public void modifyCourseEvaluation() throws Exception{
    //given
        User user =userRepository.findByUsername("user1");
        CourseEvaluation courseEvaluation = courseEvaluationRepository.findById(courseEvaluation1Id).get();
    //when
        boolean result = courseEvaluationService.modifyCourseEvaluation(courseEvaluation1Id.toString(), "updatecomments", "4.5", user,course1Id.toString());
        CourseEvaluation findCourseEvaluation = courseEvaluationRepository.findById(courseEvaluation1Id).get();
        //then
        assertThat(result).isEqualTo(true);
        assertThat(findCourseEvaluation).extracting("comments","scope")
                .containsOnly("updatecomments",4.5);


    }
}