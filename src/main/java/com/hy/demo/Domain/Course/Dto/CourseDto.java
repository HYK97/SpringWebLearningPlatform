package com.hy.demo.Domain.Course.Dto;

import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Utils.ObjectUtils;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;


@Data
@NoArgsConstructor
public class CourseDto {

    private Long id;

    private String courseName;

    private CourseUser user;

    private String teachName;

    private String thumbnail;

    private String courseExplanation;

    private Date createDate;

    private Double scope;

    private Double starScope;

    private Long reviewCount;

    private int[] score = new int[]{5, 4, 3, 2, 1};

    private List<Double> starPercent;

    private Long userJoinCount;

    public Course returnEntity() {
        return Course.builder()
                .courseExplanation(this.courseExplanation)
                .thumbnail(this.thumbnail)
                .id(this.id)
                .teachName(this.teachName)
                .build();
    }

    public void setUser(User user) {
        this.user = new CourseUser(user.getUsername(), user.getEmail(), user.getRole());
    }

    public CourseDto(Long id, String courseName, User user, Timestamp createDate, String teachName, String thumbnail, String courseExplanation, Double scope, Long reviewCount) {
        this.id = id;
        this.courseName = courseName;
        this.user = new CourseUser(user.getUsername(), user.getEmail(), user.getRole());
        this.createDate = new Date(createDate.getTime());
        this.teachName = teachName;
        this.thumbnail = thumbnail;
        this.courseExplanation = courseExplanation;
        if (!ObjectUtils.isEmpty(scope)) {
            this.scope = (Math.round(scope * 10) / 10.0);
            this.starScope = this.scope * 20;
            this.starScope += 1.5;
        } else {
            this.scope = 0.0;
            this.starScope = 0.0;
        }
        this.reviewCount = reviewCount;
    }

    public CourseDto(Long id, String courseName, Timestamp createDate, String teachName, Double scope, Long userJoinCount) {
        this.id = id;
        this.courseName = courseName;
        this.createDate = new Date(createDate.getTime());
        this.teachName = teachName;
        if (!ObjectUtils.isEmpty(scope)) {
            this.scope = (Math.round(scope * 10) / 10.0);
        } else {
            this.scope = 0.0;
        }
        this.userJoinCount = userJoinCount;
    }


}
