package com.hy.demo.Domain.User.Contoller;

import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
@WithMockUser
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


    private MockMvc mvc;

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
                .andExpect(view().name("/user/loginForm"));
    }

    @Test
    public void joinFormTest() throws Exception {
        // when
        mvc.perform(get("/joinForm"))
                .andDo(print())
                // then
                .andExpect(handler().methodName("joinForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/joinForm"));
    }


    //아이디 중복추가후
   /* @Test
    public void joinPost() throws Exception {
        //given
        ObjectMapper objectMapper=new ObjectMapper();

        User user = User.builder()
                .username("test")
                .role("ROLE_USER")
                .email("test@gmail.com")
                .password(passwordEncoder.encode("test"))
                .build();
        
        String content = objectMapper.writeValueAsString(user);

        // when
        mvc.perform(post("/join")
                .param("user",content)
                    )
                .andDo(print())
                .andExpect(jsonPath());
                // then

    }*/



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