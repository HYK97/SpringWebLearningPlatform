package com.hy.demo.Domain.Community.Repository;

import com.hy.demo.Domain.Community.Dto.CommunityDto;
import com.hy.demo.Domain.Community.Entity.Community;
import com.hy.demo.Domain.Course.Entity.Course;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommunityRepositoryImplTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    Logger logger;

    private Long courseId1;

    private Long courseId2;


    @BeforeEach
    public void setup() {

        User manager1 = User.builder()
                .username("manager1")
                .role("ROLE_MANAGER")
                .email("manager1@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();
        User manager2 = User.builder()
                .username("manager2")
                .role("ROLE_MANAGER")
                .email("manager2@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();

        Course course1 = Course.builder()
                .courseName("test1")
                .user(manager1)
                .build();
        Course course2 = Course.builder()
                .courseName("test1")
                .user(manager2)
                .build();

        Community community1 = Community.builder()
                .course(course1)
                .contents("test1")
                .user(manager1)
                .title("title1")
                .build();

        Community community2 = Community.builder()
                .course(course1)
                .contents("test2")
                .user(manager1)
                .title("title2")
                .build();

        Community community3 = Community.builder()
                .course(course1)
                .contents("re1")
                .user(manager2)
                .title("re1")
                .build();


        Community community4 = Community.builder()
                .course(course2)
                .contents("re2")
                .user(manager2)
                .title("re2")
                .build();


        Community community5 = Community.builder()
                .course(course2)
                .contents("re3")
                .user(manager1)
                .title("re3")
                .build();
        userRepository.save(manager1);
        userRepository.save(manager2);
        courseId1=courseRepository.save(course1).getId();
        courseId2=courseRepository.save(course2).getId();
        communityRepository.save(community1);
        communityRepository.save(community2);
        communityRepository.save(community3);
        communityRepository.save(community4);
        communityRepository.save(community5);

    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
        communityRepository.deleteAll();
    }


    @Test
    public void findByUserIdAndCourseName() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        //when
        Page<CommunityDto> CommunityDto = communityRepository.findByCourseIdAndSearch(courseId1, pageRequest, "re");
        Page<CommunityDto> CommunityDto2 = communityRepository.findByCourseIdAndSearch(courseId2, pageRequest, "");
        List<CommunityDto> content = CommunityDto.getContent();
        List<CommunityDto> content2 = CommunityDto2.getContent();
        //then
        assertThat(content.size()).isEqualTo(1);
        assertThat(content2.size()).isEqualTo(2);
        assertThat(content).extracting("title", "user.username","contents")
                .containsOnly(
                        tuple("re1", "manager2","re1")
                );
        assertThat(content2).extracting("title", "user.username","contents")
                .containsOnly(
                        tuple("re2", "manager2","re2"),
                        tuple("re3", "manager1","re3")
                );

    }



}