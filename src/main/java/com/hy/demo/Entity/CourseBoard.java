package com.hy.demo.Entity;

import javax.persistence.*;

public class CourseBoard extends BaseEntity{
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CourseBoard_id")
    private Long id;
    private String title;
    private String contents;
    private int views;
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Course_id")
    private Course course;
}
