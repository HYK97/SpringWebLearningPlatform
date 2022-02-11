package com.hy.demo.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Comments extends BaseEntity{

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

    public void addComments(CourseBoard courseBoard,User user) {
        this.courseBoard =courseBoard;
        this.user=user;
        courseBoard.getComments().add(this);
        user.getComments().add(this);
    }



}
