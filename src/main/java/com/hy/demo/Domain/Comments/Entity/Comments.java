package com.hy.demo.Domain.Comments.Entity;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.User.Entity.User;
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
    @Column(name="Comments_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CourseBoard_id")
    private CourseBoard courseBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Reply_Group_id")
    private Comments parent;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comments> children;

    private String comments;

    public void addComments(CourseBoard courseBoard, User user) {
        this.courseBoard =courseBoard;
        this.user=user;
        courseBoard.getComments().add(this);
        user.getComments().add(this);
    }

    public CommentsDto changeDto() {
        CommentsDto commentsDto =new CommentsDto();
        commentsDto.setComments(this.comments);
        commentsDto.setCreateDate( new Date(this.getCreateDate().getTime()));
        commentsDto.setId(this.id);
        return commentsDto;
    }
    public void updateComments(String comments) {
        this.comments = comments;
    }


}
