package com.hy.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
///a7d22101-707e-4eff-9994-5f26983d5520
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean(name = "uploadPath")
	public String uploadPath() {
		return "D:/image/";
	}
}
