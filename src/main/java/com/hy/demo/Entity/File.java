package com.hy.demo.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Not;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class File {

    @Id
    @Column(name = "File_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="CourseBoard_id")
    private CourseBoard courseBoard;


    @ManyToOne
    @JoinColumn(name ="Notice_id")
    private Notice notice;


    @Column(nullable = false)
    private String origFileName;  // 파일 원본명

    @Column(nullable = false)
    private String filePath;  // 파일 저장 경로

    private Long fileSize;


    public void addFile(CourseBoard courseBoard,Notice notice) {
        this.courseBoard =courseBoard;
        this.notice=notice;
        notice.getFile().add(this);
        courseBoard.getFiles().add(this);
    }


}