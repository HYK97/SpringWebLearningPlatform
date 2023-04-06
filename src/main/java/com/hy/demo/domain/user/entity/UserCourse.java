package com.hy.demo.domain.user.entity;

import com.hy.demo.domain.BaseEntity;
import com.hy.demo.domain.course.entity.Course;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(uniqueConstraints = {@UniqueConstraint(name = "UserCourseUnique", columnNames = {"User_id", "Course_id"})})
public class UserCourse extends BaseEntity {

    @Id // primary key
    @Column(name = "UserCourse_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Course_id")
    private Course course;


    //유저 코스 넣을때 유저의 리스트에도 포함되게
    public void addUserCourse(User user, Course course) {
        this.user = user;
        this.course = course;
        user.getUserCourses().add(this);
        course.getUserCourses().add(this);
    }

}
