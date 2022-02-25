package com.hy.demo.Test;

import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PostBean  implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //테스트유저
        User user = User.builder()
                .username("tuser")
                .role("ROLE_USER")
                .email("user@gmail.com")
                .password(passwordEncoder.encode("user"))
                .build();

        User manager = User.builder()
                .username("tmanager")
                .role("ROLE_MANAGER")
                .email("manager@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();

        userRepository.save(user);
        userRepository.save(manager);
    }
}
