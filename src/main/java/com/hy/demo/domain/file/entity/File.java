package com.hy.demo.domain.file.entity;

import com.hy.demo.domain.board.entity.CourseBoard;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class File {

    @Id
    @Column(name = "File_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CourseBoard_id")
    private CourseBoard courseBoard;


    @Column(nullable = false)
    private String origFileName;  // 파일 원본명

    @Column(nullable = false)
    private String filePath;  // 파일 저장 경로

    private Long fileSize;


    public void addFile(CourseBoard courseBoard) {
        this.courseBoard = courseBoard;
        courseBoard.getFiles().add(this);
    }


}