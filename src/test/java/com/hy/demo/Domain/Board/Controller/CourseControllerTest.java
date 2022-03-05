package com.hy.demo.Domain.Board.Controller;

import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import com.hy.demo.Domain.Board.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Board.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Long testCode;

    private MockMvc mvc;

    @PostConstruct
    public void accountSetup() {
        testCode = userRepository.save(User.builder()
                .username("test")
                .email("test@com")
                .role("ROLE_USER")
                .password("password").build()).getId();
    }


    private Course course;
    private Course course2;


    @BeforeEach
    public void setup(){

        mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        User user = User.builder()
                .username("user")
                .role("ROLE_USER")
                .email("user@gmail.com")
                .password(passwordEncoder.encode("user"))
                .build();

        User manager = User.builder()
                .username("manager")
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

        course = Course.builder()
                .courseName("1")
                .teachName("tmt")
                .courseExplanation("sdasd")
                .user(manager)
                .build();
        course2 = Course.builder()
                .courseName("2")
                .teachName("ffaa")
                .courseExplanation("ffaa")
                .user(manager2)
                .build();

        CourseEvaluation courseEvaluation1 = CourseEvaluation.builder()
                .comments("Sdsd")
                .scope(4.0)
                .course(course)
                .user(user)
                .build();

        CourseEvaluation courseEvaluation2 = CourseEvaluation.builder()
                .comments("Sdsd")
                .scope(2.0)
                .course(course)
                .user(user)
                .build();
        CourseEvaluation courseEvaluation3 = CourseEvaluation.builder()
                .comments("Sdsd")
                .scope(3.5)
                .course(course)
                .user(user)
                .build();

        userRepository.save(user);
        userRepository.save(manager);
        userRepository.save(manager2);

        courseRepository.save(course);
        courseRepository.save(course2);

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
    @WithMockUser
    public void viewAccessSuccessTest() throws Exception {
        //given

        PageRequest page = PageRequest.of(0,9 );
        List<CourseDto> findDto=courseRepository.findByCourseNameAndUserDTO("",page).getContent();
        List<CourseDto> sortDto=new ArrayList<>();
        ListIterator li = findDto.listIterator(findDto.size());
        while(li.hasPrevious()) {
            sortDto.add((CourseDto) li.previous());
        }
        //웹에서는 역으로나오기떄문에 바꿔줘야함.
        // when
        mvc.perform(get("/course/view"))
                .andDo(print())
                // then
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalPages",1))
                .andExpect(model().attribute("Previous",false))
                .andExpect(model().attribute("Next",false))
                .andExpect(model().attribute("totalElements",2L))
                .andExpect(model().attribute("course",sortDto));
        // then
    }
    @Test
    @WithAnonymousUser
    public void viewAccessFailTest() throws Exception {
        //given
        // when
        mvc.perform(get("/course/view"))
                .andDo(print())
                // then
                .andExpect(status().is4xxClientError());

    }


    @Test
    @WithMockUser
    public void viewSearchSuccessTest() throws Exception {
        //given
        PageRequest page = PageRequest.of(0,9 );
        List<CourseDto> findDto=courseRepository.findByCourseNameAndUserDTO("2",page).getContent();

        // when
        mvc.perform(get("/course/search").param("search","2"))
                .andDo(print())
                // then
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalPages",1))
                .andExpect(model().attribute("Previous",false))
                .andExpect(model().attribute("Next",false))
                .andExpect(model().attribute("totalElements",1L))
                .andExpect(model().attribute("course",findDto));

    }



}