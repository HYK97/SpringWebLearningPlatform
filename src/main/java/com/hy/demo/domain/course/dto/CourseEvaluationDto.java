package com.hy.demo.domain.course.dto;

import com.hy.demo.domain.user.dto.UserDto;
import com.hy.demo.domain.user.entity.User;
import com.hy.demo.utils.ObjectUtils;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@ToString
public class CourseEvaluationDto {

    private Long id;

    private String courseName;

    private Long courseID;

    private Double scope;

    private Double starScope;

    private String comments;

    private String reply;

    private Long replyId;

    private Date createDate;

    private Date replyCreateDate;


    private UserDto user;

    public CourseEvaluationDto(Long id, String courseName, User user, Long courseID, Double scope, String comments, Timestamp createDate, String reply, Timestamp replyCreateDate, Long replyId) {
        this.id = id;
        this.courseName = courseName;
        this.user = user.changeDto();
        this.courseID = courseID;
        this.scope = scope;
        this.comments = comments;
        if (!ObjectUtils.isEmpty(scope)) {
            this.scope = (Math.round(scope * 10) / 10.0);
            this.starScope = this.scope * 20;
            this.starScope += 1.5;
        } else {
            this.scope = 0.0;
            this.starScope = 0.0;
        }

        this.createDate = new Date(createDate.getTime());
        this.reply = reply;
        if (!ObjectUtils.isEmpty(replyCreateDate)) {
            this.replyCreateDate = new Date(replyCreateDate.getTime());
        } else
            this.replyCreateDate = null;

        this.replyId = replyId;

    }

}
