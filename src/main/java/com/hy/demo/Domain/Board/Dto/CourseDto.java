package com.hy.demo.Domain.Board.Dto;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.Board.Entity.Comments;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Entity.Notice;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Entity.UserCourse;
import com.hy.demo.Utils.ObjectUtils;
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

    private String thumbnail;

    private String courseExplanation;

    private Timestamp createDate;

    private Double scope;

    private Double starscope;


    public CourseDto(Long id, String courseName, User user,Timestamp createDate,String teachName,String thumbnail,String courseExplanation,Double scope) {
        this.id = id;
        this.courseName = courseName;
        this.user =new CourseUser(user.getUsername(),user.getEmail(),user.getRole());
        this.createDate =createDate;
        this.teachName =teachName;
        this.thumbnail =thumbnail;
        this.courseExplanation =courseExplanation;
        if (!ObjectUtils.isEmpty(scope)) {
            this.scope = (Math.round(scope*10)/10.0);
            this.starscope =this.scope*20;
            this.starscope +=1.5;
        } else {
            this.scope = 0.0;
            this.starscope =0.0;
        }

    }




}
