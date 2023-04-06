package com.hy.demo.domain.comments.entity;

import com.hy.demo.domain.BaseEntity;
import com.hy.demo.domain.board.entity.CourseBoard;
import com.hy.demo.domain.comments.dto.CommentsDto;
import com.hy.demo.domain.community.entity.Community;
import com.hy.demo.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comments extends BaseEntity {

    @Id // primary key
    @Column(name = "Comments_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CourseBoard_id")
    private CourseBoard courseBoard;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Community_id")
    private Community community;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Reply_Group_id")
    private Comments parent;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> children;
    @Column(length = 100000000)
    private String comments;

    public void addComments(CourseBoard courseBoard, User user) {
        this.courseBoard = courseBoard;
        this.user = user;
        courseBoard.getComments().add(this);
        user.getComments().add(this);
    }

    public CommentsDto changeDto() {
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setComments(this.comments);
        commentsDto.setCreateDate(new Date(this.getCreateDate().getTime()));
        commentsDto.setId(this.id);
        return commentsDto;
    }

    public void updateComments(String comments) {
        this.comments = comments;
    }


}
