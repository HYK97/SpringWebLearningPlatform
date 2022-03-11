package com.hy.demo.Domain.Board.Entity;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.User.Entity.User;
import lombok.*;

import javax.persistence.*;

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

    private String comments;

    public void addComments(CourseBoard courseBoard, User user) {
        this.courseBoard =courseBoard;
        this.user=user;
        courseBoard.getComments().add(this);
        user.getComments().add(this);
    }



}
