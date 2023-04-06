package com.hy.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Configuration
public class AppConfig {

    @Bean(name = "uploadPath")
    public String uploadPath() {
        LocalDate now = LocalDate.now();
        String Path = "resources/image/" + now;
        return Path;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
