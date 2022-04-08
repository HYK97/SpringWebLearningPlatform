package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Entity.UserCourse;
import com.hy.demo.Domain.User.Repository.UserCourseRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    Logger logger;

    Long course1Id;
    Long course2Id;
    Long managerId1;

    @BeforeEach
    public void setup() {


        User manager1 = User.builder()
                .username("manager1")
                .role("ROLE_MANAGER")
                .email("manager@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();

        User user1 = User.builder()
                .username("user1")
                .role("ROLE_USER")
                .email("user1@gmail.com")
                .password(passwordEncoder.encode("user1"))
                .build();

        User user2 = User.builder()
                .username("user2")
                .role("ROLE_USER")
                .email("user2@gmail.com")
                .password(passwordEncoder.encode("user2"))
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

        UserCourse userCourse1 = UserCourse.builder()
                .course(course1)
                .user(user1)
                .build();

        UserCourse userCourse2 = UserCourse.builder()
                .course(course1)
                .user(user2)
                .build();

        UserCourse userCourse3 = UserCourse.builder()
                .course(course2)
                .user(user1)
                .build();


        managerId1 = userRepository.save(manager1).getId();
        userRepository.save(manager2);
        userRepository.save(user1);
        userRepository.save(user2);

        course1Id = courseRepository.save(course1).getId();
        course2Id = courseRepository.save(course2).getId();
        courseRepository.save(course3);
        courseRepository.save(course4);

        userCourseRepository.save(userCourse1);
        userCourseRepository.save(userCourse2);
        userCourseRepository.save(userCourse3);

    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    public void searchCourse() {
        //when
        //강좌명으로검색
        AssertCourse(0, 3, "test", 3, "User", "username", new String[]{"manager1", "manager1", "manager2"}, "courseName", new String[]{"test1", "test2", "test3"});
        AssertCourse(0, 2, "test", 2, "User", "username", new String[]{"manager1", "manager1"}, "courseName", new String[]{"test1", "test2"});
        AssertCourse(0, 3, "english", 1, "User", "username", new String[]{"manager2"}, "courseName", new String[]{"english"});
        AssertCourse(0, 3, "false", 0, "User", "username", new String[]{}, "courseName", new String[]{});
        AssertCourse(0, 3, "", 3, "User", "username", new String[]{"manager1", "manager1", "manager2"}, "courseName", new String[]{"test1", "test2", "test3"});

        //강사명으로 검색
        AssertCourse(0, 3, "manager2", 2, "teachName", "", new String[]{"manager2", "manager2"}, "courseName", new String[]{"english", "test3"});
        AssertCourse(0, 3, "manager1", 2, "teachName", "", new String[]{"manager1", "manager1"}, "courseName", new String[]{"test1", "test2"});
    }

    private void AssertCourse(int page, int size, String courseName, int resultSize, String extracting1, String extracting2, String[] contains1, String extracting3, String[] contains2) {
        //given
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CourseDto> findCourse = courseRepository.findCourseDtoByCourseName(courseName, pageRequest);
        //then
        assertThat(findCourse.getContent().size()).isEqualTo(resultSize);
        if (extracting2.equals("")) {
            assertThat(findCourse.getContent())
                    .extracting(extracting1)
                    .contains(contains1);
        } else {
            assertThat(findCourse.getContent())
                    .extracting(extracting1)
                    .extracting(extracting2)
                    .contains(contains1);
        }
        assertThat(findCourse.getContent())
                .extracting(extracting3)
                .contains(contains2);
    }


    @Test
    public void count() throws Exception {
        //given
        Long count = courseRepository.count();
        //when
        assertThat(count)
                //then
                .isEqualTo(4);
    }

    @Test
    public void findByRandomId() throws Exception {
        //given
        int amount = 2;
        //when
        List<CourseDto> findRandomCourse = courseRepository.findByRandomId(2);
        //then
        assertThat(findRandomCourse.size()).isEqualTo(2);
    }

    @Test
    public void findByUserIdAndCourseName() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);
        //when
        Page<CourseDto> CourseDto = courseRepository.findByUserIdAndCourseName(null, managerId1, pageRequest);
        List<CourseDto> content = CourseDto.getContent();
        //then
        assertThat(content.size()).isEqualTo(2);
        assertThat(content).extracting("courseName", "teachName", "userJoinCount")
                .containsOnly(
                        tuple("test1", "manager1", 2L),
                        tuple("test2", "manager1", 1L)
                );

    }

}