package com.hy.demo.Domain.User.Entity;


import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.Comments.Entity.Comments;
import com.hy.demo.Domain.Community.Entity.Community;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.User.Dto.UserDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_id")
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN
    private String profileImage;
    @Column(length = 100000000)
    private String selfIntroduction;
    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCourse> userCourses = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Community> community = new ArrayList<>();

    // 구글,페이스북,카카오등으로 가입됬을때
    private String provider;
    private String providerId;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", provider='" + provider + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }

    public UserDto changeDto() {
        UserDto userdto = new UserDto();
        userdto.setEmail(this.email);
        userdto.setUsername(this.username);
        userdto.setProfileImage(this.profileImage);
        userdto.setSelfIntroduction(this.selfIntroduction);
        userdto.setNickname(this.nickname);
        userdto.setRole(this.role);
        return userdto;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}