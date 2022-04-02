package com.hy.demo.Domain.Comments.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class CommentsDto {


    private Long id;

    private String username;

    private String comments;

    private Integer myCommentsFlag = null;

    private Date createDate;

    private Long replyCounts;

    private Long replyId;

    private String profileImage;


    public CommentsDto(Long id, String username, String comments, Timestamp createDate, Long replyCounts, String profileImage) {
        this.id = id;
        this.username = username;
        this.comments = comments;
        this.createDate = new Date(createDate.getTime());
        this.replyCounts = replyCounts;
        this.profileImage = profileImage;
    }

    public CommentsDto(String username, Long id, String comments, Timestamp createDate, Long replyId, String profileImage) {
        this.username = username;
        this.id = id;
        this.comments = comments;
        this.createDate = new Date(createDate.getTime());
        this.replyId = replyId;
        this.profileImage = profileImage;
    }
}
