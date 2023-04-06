package com.hy.demo.domain.comments.dto;

import com.hy.demo.domain.user.dto.UserDto;
import com.hy.demo.domain.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class CommentsDto {


    private Long id;

    private String comments;

    private Integer myCommentsFlag = null;

    private Date createDate;

    private Long replyCounts;

    private Long replyId;

    private UserDto user;


    public CommentsDto(Long id, User user, String comments, Timestamp createDate, Long replyCounts) {
        this.id = id;
        this.user = user.changeDto();
        this.comments = comments;
        this.createDate = new Date(createDate.getTime());
        this.replyCounts = replyCounts;
    }

    public CommentsDto(Long id, String comments, Timestamp createDate, Long replyId, User user) {
        this.id = id;
        this.comments = comments;
        this.createDate = new Date(createDate.getTime());
        this.replyId = replyId;
        this.user = user.changeDto();
    }
}
