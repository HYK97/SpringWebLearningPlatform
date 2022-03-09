package com.hy.demo.Domain.Course.Entity;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.User.Entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class CourseEvaluation extends BaseEntity {
    @Id // primary key
    @Column(name = "CourseEvalution_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Course_id")
    private Course course;

    private Double scope;

    private String comments;

    @Column(unique=true)
    private Long replyId;

    public void updateCourseEvaluation(String comments, Double scope) {
        this.comments = comments;
        this.scope = scope;
    }

    public void updateReply(String comments) {
        this.comments = comments;

    }




}