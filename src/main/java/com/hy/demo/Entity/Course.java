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
    private User userId;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserCourse> userCourses = new ArrayList<>();

    private int heart;

    public void addCourse(User userId) {
        this.userId =userId;
        userId.getCourses().add(this);
    }




}
