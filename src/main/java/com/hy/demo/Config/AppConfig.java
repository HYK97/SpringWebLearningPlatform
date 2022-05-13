package com.hy.demo.Config;

import com.querydsl.core.annotations.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Bean
    Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
