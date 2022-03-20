package com.hy.demo.Domain.Course.Entity;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Course.Dto.CourseDto;
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

    private String teachName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id")
    private User user;

    @Column(length = 100000000)
    private String courseExplanation;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CourseEvaluation> courseEvaluations = new ArrayList<>();

    private String thumbnail;
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserCourse> userCourses = new ArrayList<>();

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CourseBoard> courseBoards = new ArrayList<>();

    //코스 저장될때 유저의 리스트에도 저장되게함
    public void addCourse(User user) {
        this.user =user;
        user.getCourses().add(this);
    }

    public void updateCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void updateTeachName(String teachName) {
        this.teachName = teachName;
    }

    public void updateCourseExplanation(String courseExplanation) {
        this.courseExplanation = courseExplanation;
    }

    public void updateThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public CourseDto returnDto() {
        CourseDto courseDto =new CourseDto();
        courseDto.setCourseName(courseName);
        courseDto.setCourseExplanation(courseExplanation);
        courseDto.setUser(user);
        courseDto.setTeachName(teachName);
        courseDto.setThumbnail(thumbnail);
        return courseDto;
    }


}
