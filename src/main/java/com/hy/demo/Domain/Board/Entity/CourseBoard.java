package com.hy.demo.Domain.Board.Entity;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.Course.Entity.Course;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseBoard extends BaseEntity {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CourseBoard_id")
    private Long id;
    private String title;
    private String contents;
    private int views;

    @OneToMany(mappedBy = "courseBoard",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Course_id")
    private Course course;

    @OneToMany(mappedBy = "courseBoard",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comments> comments =new ArrayList<>();

    public void addBoard(Course course) {
        this.course=course;
        course.getCourseBoards().add(this);
    }

}
