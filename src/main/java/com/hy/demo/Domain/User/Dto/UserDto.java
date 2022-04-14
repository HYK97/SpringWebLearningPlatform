package com.hy.demo.Domain.User.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {


    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String provider;
    private String providerId;
    private String profileImage;
    private String selfIntroduction;
    private String nickname;

    public UserDto(Long id, String username, String profileImage, String selfIntroduction, String nickname) {
        this.id = id;
        this.username = username;
        this.profileImage = profileImage;
        this.selfIntroduction = selfIntroduction;
        this.nickname = nickname;
    }
}
