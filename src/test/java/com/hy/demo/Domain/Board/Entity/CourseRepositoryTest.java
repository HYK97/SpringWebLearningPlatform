package com.hy.demo.Domain.Board.Entity;

import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseRepositoryTest {


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
                .heart(0)
                .build();

        Course course2 = Course.builder()
                .courseName("test2")
                .user(manager1)
                .teachName("manager1")
                .heart(0)
                .build();

        Course course3 = Course.builder()
                .courseName("test3")
                .user(manager2)
                .teachName("manager2")
                .heart(0)
                .build();
        Course course4 = Course.builder()
                .courseName("english")
                .user(manager2)
                .teachName("manager2")
                .heart(0)
                .build();

        userRepository.save(manager1);
        userRepository.save(manager2);

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        courseRepository.save(course4);

    }

    @Test
    public void searchCourse() {

        //강좌명으로검색
        AssertCourse(0,3,"test",3,"User","username",new String[] {"manager1","manager1","manager2"},"courseName",new String[] {"test1","test2","test3"});
        AssertCourse(0,2,"test",2,"User","username",new String[] {"manager1","manager1"},"courseName",new String[] {"test1","test2"});
        AssertCourse(0,3,"english",1,"User","username",new String[] {"manager2"},"courseName",new String[] {"english"});
        AssertCourse(0,3,"false",0,"User","username",new String[]{},"courseName",new String[]{});
        AssertCourse(0,3,"",3,"User","username",new String[] {"manager1","manager1","manager2"},"courseName",new String[] {"test1","test2","test3"});

        //강사명으로 검색
        AssertCourse(0,3,"manager2",2,"teachName","",new String[]{"manager2","manager2"},"courseName",new String[]{"english","test3"});
        AssertCourse(0,3,"manager1",2,"teachName","",new String[]{"manager1","manager1"},"courseName",new String[]{"test1","test2"});
    }

    private void AssertCourse(int page,int size,String courseName,int resultSize,String extracting1,String extracting2,String []contains1,String extracting3,String []contains2) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CourseDto> findCourse = courseRepository.findByCourseNameAndUserDTO(courseName, pageRequest);
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


}