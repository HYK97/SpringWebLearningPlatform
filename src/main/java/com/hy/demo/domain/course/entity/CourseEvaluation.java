package com.hy.demo.domain.course.entity;

import com.hy.demo.domain.BaseEntity;
import com.hy.demo.domain.user.entity.User;
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
    @Column(length = 100000000)
    private String comments;

    @Column(unique = true)
    private Long replyId;

    public void updateCourseEvaluation(String comments, Double scope) {
        this.comments = comments;
        this.scope = scope;
    }

    public void updateReply(String comments) {
        this.comments = comments;

    }


}