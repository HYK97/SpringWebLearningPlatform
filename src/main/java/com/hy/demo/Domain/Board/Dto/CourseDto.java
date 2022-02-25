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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Data
public class CourseDto extends BaseEntity {

    private Long id;

    private String courseName;

    private CourseUser user;

    private String teachName;

    private int heart;

    private String thumbnail;

    private String courseExplanation;

    private Timestamp createDate;


    public CourseDto(Long id, String courseName, User user, int heart,Timestamp createDate,String teachName,String thumbnail,String courseExplanation) {
        this.id = id;
        this.courseName = courseName;
        this.user =new CourseUser(user.getUsername(),user.getEmail(),user.getRole());
        this.heart = heart;
        this.createDate =createDate;
        this.teachName =teachName;
        this.thumbnail =thumbnail;
        this.courseExplanation =courseExplanation;
    }




}
