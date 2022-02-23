package com.hy.demo.Domain.Board.Dto;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.Board.Entity.Comments;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Entity.Notice;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Entity.UserCourse;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
public class CourseDto extends BaseEntity {

    private Long id;

    private String courseName;

    private CourseUser user;

    private int heart;

    public CourseDto(Long id, String courseName, User user, int heart) {
        this.id = id;
        this.courseName = courseName;
        this.user =new CourseUser(user.getUsername(),user.getEmail(),user.getRole());
        this.heart = heart;
    }




}
