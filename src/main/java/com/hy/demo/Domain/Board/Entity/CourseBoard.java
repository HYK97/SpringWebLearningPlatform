package com.hy.demo.Domain.Board.Entity;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.File.Entity.File;
import com.hy.demo.Utils.ObjectUtils;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;

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
    private Long views;

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

    public CourseBoardDto changeDto() {
        CourseBoardDto courseBoardDto = new CourseBoardDto();
        courseBoardDto.setCourseName(this.course.getCourseName());
        courseBoardDto.setTitle(this.title);
        if (!isEmpty(files)) {
            courseBoardDto.changeFileDto(this.files);
        }
        courseBoardDto.setContents(this.contents);
        courseBoardDto.changeDate(this.getCreateDate());
        courseBoardDto.setId(this.id);
        courseBoardDto.setTeachName(this.course.getTeachName());
        courseBoardDto.setViews(this.views);
        return courseBoardDto;
    }

    public void updateCourseBoard(String title,String contents) {
        this.title =title;
        this.contents =contents;
    }

}
