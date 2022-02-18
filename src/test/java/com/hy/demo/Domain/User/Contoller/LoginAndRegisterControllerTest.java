package com.hy.demo.Domain.User.Contoller;

import com.hy.demo.DemoApplication;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import com.hy.demo.Domain.User.Service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;	// {1}
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginAndRegisterControllerTest {


    @Test
    public void test() {

    }
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
                .password("{bcrypt}manager")
                .build();
        User admin = User.builder()
                .username("admin")
                .role("ROLE_ADMIN")
                .email("admin@gmail.com")
                .password("{bcrypt}admin")
                .build();


        userRepository.save(user);
        userRepository.save(manager);
        userRepository.save(admin);
    }



    @Test
    public void loginPost() throws Exception {
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


}