package com.hy.demo.Domain.Course.Dto;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)

public class CourseUser {
    public CourseUser(String username, String email, String role,String nickname) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
    }

    private String username;
    private String email;
    private String role;
    private String nickname;



}
