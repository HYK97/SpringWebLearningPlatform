package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.entry;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Course course1;

    private Long course1Id;
    private Long course2Id;
    private Long courseEvaluation4Id;
    private CourseEvaluation courseEvaluation4;

    @BeforeEach
    public void setup(){

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
                .teachName("manager1")
                .build();

        Course course2 = Course.builder()
                .courseName("courseTest2")
                .user(manager1)
                .teachName("manager1")
                .build();

        Course course3 = Course.builder()
                .courseName("courseTest3")
                .user(manager2)
                .teachName("manager2")
                .build();
        Course course4 = Course.builder()
                .courseName("courseTest4")
                .user(manager2)
                .teachName("manager2")
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

        courseEvaluationRepository.save(courseEvaluation1);
        courseEvaluationRepository.save(courseEvaluation2);
        courseEvaluationRepository.save(courseEvaluation3);
        courseEvaluation4Id= courseEvaluationRepository.save(courseEvaluation4).getId();


        CourseEvaluation reply1 = CourseEvaluation.builder()
                .user(manager1)
                .replyId(courseEvaluation4Id)
                .comments("reply1")
                .course(course2)
                .build();

        courseEvaluationRepository.save(reply1);

    }
    @AfterEach
    public void after(){
        userRepository.deleteAll();
        courseRepository.deleteAll();
        courseEvaluationRepository.deleteAll();
    }
    @Test
    public void countScope() {
        //given
        Course byCourseName = courseRepository.findByCourseName(course1.getCourseName());
        Map<String, Double> stringDoubleMap = courseEvaluationRepository.countScope(byCourseName.getId());
        //when
        assertThat(stringDoubleMap).hasSize(5)
                //then
                .contains(entry("1",0.0), entry("2",0.0), entry("3",0.5), entry("4",2.0), entry("5",0.5));
    }

    @Test
    public void findByIDCourseEvaluationDTO() throws Exception{
    //given
        // 페이지
    PageRequest page = PageRequest.of(0, 4);

    //when
        Page<CourseEvaluationDto> findEvaluation1= courseEvaluationRepository.findByIDCourseEvaluationDTO(course1Id, page);
        Page<CourseEvaluationDto> findEvaluation2= courseEvaluationRepository.findByIDCourseEvaluationDTO(course2Id, page);
        CourseEvaluationDto find=courseEvaluationRepository.findByReply(courseEvaluation4.getId());

        System.out.println("find.toString() = " + find.toString());
        //then
        assertThat(findEvaluation1.getContent().size()).isEqualTo(3);
        assertThat(findEvaluation2.getContent().size()).isEqualTo(1);
        assertThat(findEvaluation1.getContent())
                .extracting("courseName", "username","scope","comments")
                .containsOnly(
                        tuple("courseTest1","user1" ,3.5,"test1"),
                        tuple("courseTest1","user2", 4.0,"test2"),
                        tuple("courseTest1", "manager1",4.5,"test3")
                );
        assertThat(findEvaluation2.getContent())
                .extracting("courseName", "username","scope","comments","reply")
                .containsOnly(
                        tuple("courseTest2","manager2" ,1.0,"test4","reply1")
                );

    }
}