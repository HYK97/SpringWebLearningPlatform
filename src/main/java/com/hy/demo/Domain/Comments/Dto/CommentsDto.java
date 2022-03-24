package com.hy.demo.Domain.Comments.Dto;

import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Comments.Entity.Comments;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Utils.ObjectUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;

@Data
@NoArgsConstructor
public class CommentsDto {


    private Long id;

    private String username;

    private String comments;

    private Integer myCommentsFlag= null;

    private Date createDate;


    private Long replyCounts;

    public CommentsDto(Long id, String username, String comments, Timestamp createDate) {
        this.id = id;
        this.username = username;
        this.comments = comments;
        this.createDate = new Date(createDate.getTime());
    }

    public CommentsDto(Long id, String username, String comments, Timestamp createDate,Long replyCounts) {
        this.id = id;
        this.username = username;
        this.comments = comments;
        this.createDate = new Date(createDate.getTime());
        this.replyCounts =replyCounts;
    }


}
