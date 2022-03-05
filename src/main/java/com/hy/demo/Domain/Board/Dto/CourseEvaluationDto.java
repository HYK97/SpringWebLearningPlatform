package com.hy.demo.Domain.Board.Dto;

import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.User.Entity.User;
import lombok.Data;
import lombok.ToString;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@ToString
public class CourseEvaluationDto {

    private Long id;

    private String courseName;

    private String username;

    private Long courseID;

    private Long userID;

    private Double scope;

    private String comments;

    public CourseEvaluationDto(Long id, String courseName, String username, Long courseID, Long userID, Double scope, String comments) {
        this.id = id;
        this.courseName = courseName;
        this.username = username;
        this.courseID = courseID;
        this.userID = userID;
        this.scope = scope;
        this.comments = comments;
    }
}
