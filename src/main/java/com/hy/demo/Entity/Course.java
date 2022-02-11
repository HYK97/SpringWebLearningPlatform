package com.hy.demo.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor

public class Course extends BaseEntity{
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





}
