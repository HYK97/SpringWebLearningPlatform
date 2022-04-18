package com.hy.demo.Domain.Mail.Service;

import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MailServiceTest {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    MailService mailService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    Logger logger;

    @BeforeEach
    public void setup() {

        User manager1 = User.builder()
                .username("manager1")
                .role("ROLE_MANAGER")
                .email("ddh963963@naver.com")
                .password(passwordEncoder.encode("manager"))
                .build();

        userRepository.save(manager1);


    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();

    }

    @Test
    void successSendPasswordMail() {

        //given
        String username = "manager1";
        //when,then
        assertDoesNotThrow(() -> {
            mailService.sendPasswordMail("manager1");
        });
    }

}