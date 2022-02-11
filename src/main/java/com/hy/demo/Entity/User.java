package com.hy.demo.Entity;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
public class User extends BaseEntity{
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_id")
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserCourse> userCourses = new ArrayList<>();


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Notice> notices = new ArrayList<>();

    // 구글,페이스북,카카오등으로 가입됬을때
    private String provider;
    private String providerId;


    public User(Long id, String username, String password, String email, String role, List<Course> courses, List<UserCourse> userCourses, List<Comments> comments, List<Notice> notices, String provider, String providerId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.courses = courses;
        this.userCourses = userCourses;
        this.comments = comments;
        this.notices = notices;
        this.provider = provider;
        this.providerId = providerId;
    }
}