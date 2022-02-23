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

public class Course extends BaseEntity {
    @Id // primary key
    @Column(name = "Course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id")
    private User user;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserCourse> userCourses = new ArrayList<>();

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CourseBoard> courseBoards = new ArrayList<>();

    private int heart;

    //코스 저장될때 유저의 리스트에도 저장되게함
    public void addCourse(User user) {
        this.user =user;
        user.getCourses().add(this);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", user=" + user +
                ", heart=" + heart +
                '}';
    }
}
