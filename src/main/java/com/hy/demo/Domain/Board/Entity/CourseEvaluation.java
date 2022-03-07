package com.hy.demo.Domain.Board.Entity;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Entity.UserCourse;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(uniqueConstraints={@UniqueConstraint(name = "UserCourseEvaluationUnique" ,columnNames={"Course_id","User_id"})}) //코스하나당 한개의 수강평만남길수있음.
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




}