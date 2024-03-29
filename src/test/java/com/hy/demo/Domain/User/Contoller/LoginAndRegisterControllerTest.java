package com.hy.demo.Domain.User.Contoller;

import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest

class LoginAndRegisterControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;


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
                .nickname("11")
                .password("password").build()).getId();
    }
    @AfterTransaction
    public void accountCleanup() {
        userRepository.deleteById(testCode);
    }

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
        User admin = User.builder()
                .username("admin")
                .role("ROLE_ADMIN")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("admin"))
                .build();


        userRepository.save(user);
        userRepository.save(manager);
        userRepository.save(admin);
    }


    @AfterEach
    public void after(){
     userRepository.deleteAll();
    }


    @Test
    public void loginPostTest() throws Exception {
        // given

        String userId = "user";
        String password = "user";

        // when
        mvc.perform(formLogin().user(userId).password(password))
                .andDo(print())
                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/loginRedirect"));
    }


    @Test
    public void loginFailsTest() throws Exception {
        // given
        String userId = "user";
        String password = "a";

        // when
        mvc.perform(formLogin().user(userId).password(password))
                .andDo(print())
                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/loginFailRedirect"));
    }

    @Test
    public void loginFormTest() throws Exception {

        // when
        mvc.perform(get("/loginForm"))
                .andDo(print())
                // then
                .andExpect(handler().methodName("loginForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/loginForm"));
    }

    @Test
    public void joinFormTest() throws Exception {
        // when
        mvc.perform(get("/joinForm"))
                .andDo(print())
                // then
                .andExpect(handler().methodName("joinForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/joinForm"));

    }



    @Test
    @WithUserDetails(value = "test")
    public void joinOAuthPost() throws Exception {
        //given
        userRepository.deleteAll();
        // when
        mvc.perform(post("/join")
        .param("role","USER_ROLE"))
                .andDo(print())
                .andExpect(handler().methodName("join"))
                .andExpect(status().isOk())
                .andExpect(content().string("main/index"));
        // then

    }

    @Test
    public void joinSuccessPost() throws Exception {
        //given

        // when
        mvc.perform(post("/join")
                .param("username","abc")
                .param("password","test")
                .param("email","test@gmail.com")
                .param("role","ROLE_USER")
                .param("nickname","asc"))
                // then
                .andDo(print())
                .andExpect(handler().methodName("join"))
                .andExpect(status().isOk())
                .andExpect(content().string("loginForm"));


    }


    @Test
    public void joinDuplicationIdPost() throws Exception {
        //given
        //기존의 post 회원 아이디랑 같은 아이디로 회원가입시도
        // when
        mvc.perform(post("/join")
                .param("username","test")
                .param("password","test")
                .param("email","test@gmail.com")
                .param("role","ROLE_USER")
                .param("nickname","asd"))
                // then
                .andDo(print())
                .andExpect(handler().methodName("join"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void joinDuplicationNicknamePost() throws Exception {
        //given
        //기존의 post 회원 아이디랑 같은 아이디로 회원가입시도
        // when
        mvc.perform(post("/join")
                .param("username","dd")
                .param("password","test")
                .param("email","test@gmail.com")
                .param("role","ROLE_USER")
                .param("nickname","11"))
                // then
                .andDo(print())
                .andExpect(handler().methodName("join"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }







  /*  @Test
    public void adminTest() throws Exception {
        // when
        mvc.perform(get("/user/admin")
                .with(user("user").roles("USER")))
                .andDo(print())
                // then
                .andExpect(handler().methodName("admin"))
                .andExpect(view().name("/user/admin"));
    }
*/


}