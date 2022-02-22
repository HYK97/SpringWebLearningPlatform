package com.hy.demo.Domain.Board.Controller;

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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class MainControllerTest {

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
                .password("password").build()).getId();
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
    @WithUserDetails(value = "test")
    public void indexSuccessTest() throws Exception {

        // when
        mvc.perform(get("/main/index"))
                .andDo(print())
        // then
                .andExpect(status().isOk())
                .andExpect(view().name("/main/index"));
    }

    @Test
    @WithAnonymousUser
    public void indexFailsTest() throws Exception {

        // when
        mvc.perform(get("/main/index"))
                .andDo(print())

                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/loginForm"));

    }







}