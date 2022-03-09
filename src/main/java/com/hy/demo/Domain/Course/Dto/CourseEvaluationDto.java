package com.hy.demo.Domain.Course.Dto;

import com.hy.demo.Utils.ObjectUtils;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@ToString
public class CourseEvaluationDto {

    private Long id;

    private String courseName;

    private String username;

    private Long courseID;

    private Long userID;

    private Double scope;

    private Double starScope;

    private String comments;

    private String reply;

    private Long replyId;

    private Date createDate;

    private Date replyCreateDate;




    public CourseEvaluationDto(Long id, String courseName, String username, Long courseID, Long userID, Double scope, String comments, Timestamp createDate, String reply,Timestamp replyCreateDate,Long replyId) {
        this.id = id;
        this.courseName = courseName;
        this.username = username;
        this.courseID = courseID;
        this.userID = userID;
        this.scope = scope;
        this.comments = comments;
        if (!ObjectUtils.isEmpty(scope)) {
            this.scope = (Math.round(scope*10)/10.0);
            this.starScope =this.scope*20;
            this.starScope +=1.5;
        } else {
            this.scope = 0.0;
            this.starScope =0.0;
        }

        this.createDate =new Date(createDate.getTime());
        this.reply =reply;
        if (!ObjectUtils.isEmpty(replyCreateDate)) {
            this.replyCreateDate = new Date(replyCreateDate.getTime());
        }else
            this.replyCreateDate=null;

        this.replyId = replyId;

    }
    public CourseEvaluationDto(Long id, String courseName, String username, Long courseID, Long userID, Double scope, String comments) {
        this.id = id;
        this.courseName = courseName;
        this.username = username;
        this.courseID = courseID;
        this.userID = userID;
        this.scope = scope;
        if (!ObjectUtils.isEmpty(scope)) {
            this.scope = (Math.round(scope*10)/10.0);
            this.starScope =this.scope*20;
            this.starScope +=1.5;
        } else {
            this.scope = 0.0;
            this.starScope =0.0;
        }
        this.comments = comments;
    }
}
