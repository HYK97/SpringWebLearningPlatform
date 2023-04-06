package com.hy.demo.domain.mail.service;

import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    MailServiceImpl mailService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

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
        User findUser = userRepository.findByUsername(username);
        //when,then
        assertDoesNotThrow(() -> {
            mailService.sendMail(findUser);
        });
    }

}