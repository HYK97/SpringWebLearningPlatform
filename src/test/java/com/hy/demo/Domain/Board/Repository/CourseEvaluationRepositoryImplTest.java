package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Course.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import com.hy.demo.Domain.Course.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
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

    @Autowired
    Logger logger;

    private List<Long> userIdList = new ArrayList<Long>();
    private List<Long> courseIdList = new ArrayList<Long>();
    private List<Long> courseEvaluationIdList = new ArrayList<Long>();
    private List<Long> replyIdList = new ArrayList<Long>();
    @BeforeEach
    public void setup() {

        for (int i = 0; i < 20; i++) { // 유저 및 코스
            User user;
            Course course;
            Long userId;
            if (i % 2 == 0) {
                user = User.builder()
                        .username("user" + i)
                        .role("ROLE_USER")
                        .email("user" + i + "@gmail.com")
                        .password(passwordEncoder.encode("user" + i))
                        .build();
                userId = userRepository.save(user).getId();
                userIdList.add(userId);
            } else {
                user = User.builder()
                        .username("manager" + i)
                        .role("ROLE_MANAGER")
                        .email("manager" + i + "@gmail.com")
                        .password(passwordEncoder.encode("manager" + i))
                        .build();

                course = Course.builder()
                        .courseName("courseName" + user.getUsername())
                        .user(user)
                        .courseExplanation("코스 " + i + "번 입니다.")
                        .build();
                userId = userRepository.save(user).getId();
                userIdList.add(userId);
                Long courseId = courseRepository.save(course).getId();
                courseIdList.add(courseId);
            }
        }

        List<User> allUser = userRepository.findByRole("ROLE_USER"); //댓글
        List<Course> allCourse = courseRepository.findAll();
        for (Course course : allCourse) {
            for (int i = 0; i < 3; i++) {
                CourseEvaluation courseEvaluation = CourseEvaluation.builder()
                        .user(allUser.get(i))
                        .scope((double) (i + 8 / 3))
                        .comments("courseEvaluation" + i)
                        .course(course)
                        .build();
                Long courseEvaluationId = courseEvaluationRepository.save(courseEvaluation).getId();
                courseEvaluationIdList.add(courseEvaluationId);
                //reply
                String teachName = allUser.get(i).getUsername();
                User findManger = userRepository.findByUsername(teachName);
                CourseEvaluation reply = CourseEvaluation.builder()
                        .user(findManger)
                        .comments("reply" + i)
                        .course(course)
                        .replyId(courseEvaluationId)
                        .build();
                Long replyId = courseEvaluationRepository.save(reply).getId();
                replyIdList.add(replyId);
            }
        }


    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
        courseEvaluationRepository.deleteAll();
    }

    @Test
    public void countScope() {
        //given
        Course byCourseName = courseRepository.findById(courseIdList.get(0)).get();
        //when
        Map<String, Double> stringDoubleMap = courseEvaluationRepository.countScope(byCourseName.getId());
        //then
        assertThat(stringDoubleMap).hasSize(5)
                .contains(entry("1", 0.0), entry("2", 1.0), entry("3", 1.0), entry("4", 1.0), entry("5", 0.0));
    }

    @Test
    public void findByIDCourseEvaluationDTO() throws Exception {
        //given
        // 페이지
        PageRequest page = PageRequest.of(0, 4);
        //when
        Page<CourseEvaluationDto> findEvaluation1 = courseEvaluationRepository.findByIDCourseEvaluationDTO(courseIdList.get(0), page);
        // then
        assertThat(findEvaluation1.getContent().size()).isEqualTo(3);
        assertThat(findEvaluation1.getContent())
                .extracting("courseName", "user.username", "scope", "comments")
                .containsOnly(
                        tuple("courseNamemanager1", "user0", 2.0, "courseEvaluation0"),
                        tuple("courseNamemanager1", "user2", 3.0, "courseEvaluation1"),
                        tuple("courseNamemanager1", "user4", 4.0, "courseEvaluation2")
                );
    }


    @Test
    public void findByReply() throws Exception {
        //given
        //when
        CourseEvaluation find = courseEvaluationRepository.findByReply(courseEvaluationIdList.get(0));
        //then
        assertThat(find).extracting("comments", "replyId")
                .containsOnly(
                        "reply0", courseEvaluationIdList.get(0)
                );
    }

    @Test
    public void findByUsernameAndCourseIdAndId() throws Exception {
        //given
        String username1 = "user0";
        String username2 = "user9";
        //when
        CourseEvaluation findEvaluation = courseEvaluationRepository.findByUsernameAndCourseIdAndId(username1, courseIdList.get(0), courseEvaluationIdList.get(0));
        CourseEvaluation findEvaluation2 = courseEvaluationRepository.findByUsernameAndCourseIdAndId(username2, courseIdList.get(0), courseEvaluationIdList.get(0));
        //then
        assertThat(findEvaluation).extracting("scope", "comments")
                .containsOnly(2.0, "courseEvaluation0");
        assertThat(findEvaluation2)
                .isNull();
    }


}