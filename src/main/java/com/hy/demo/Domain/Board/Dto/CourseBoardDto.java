package com.hy.demo.Domain.Board.Dto;

import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Entity.File;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class CourseBoardDto {

    private Long id;
    private String title;
    private String contents;
    private Long views;
    private Date createDate;
    private String teachName;
    private String courseName;
    private List<FileDto> files = new ArrayList<>();
    private Long commentsCount;


    public CourseBoardDto(Long id, String title, Long views, Timestamp createDate, String contents, String teachName, String courseName) {
        this.id = id;
        this.title = title;
        this.views = views;
        this.createDate = new Date(createDate.getTime());
        this.contents = contents;
        this.teachName = teachName;
        this.courseName = courseName;
    }

    public CourseBoardDto(Long id, String title, String contents, Long views, Timestamp createDate, String teachName, String courseName, List<File> files) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.views = views;
        this.createDate = new Date(createDate.getTime());
        this.teachName = teachName;
        this.courseName = courseName;
        for (File file : files) {
            FileDto fileDto = new FileDto();
            fileDto.setFilePath(file.getFilePath());
            fileDto.setFileSize(file.getFileSize());
            fileDto.setOrigFileName(file.getOrigFileName());
            fileDto.setId(file.getId());
            this.files.add(fileDto);
        }
    }


    public CourseBoardDto(Long id, String title, Long views, Long commentsCount) {
        this.id = id;
        this.title = title;
        this.views = views;
        this.commentsCount = commentsCount;
    }

    public void changeFileDto(List<File> files) {
        for (File file : files) {
            FileDto fileDto = new FileDto();
            fileDto.setFilePath(file.getFilePath());
            fileDto.setFileSize(file.getFileSize());
            fileDto.setOrigFileName(file.getOrigFileName());
            fileDto.setId(file.getId());
            this.files.add(fileDto);
        }
    }

    public void changeDate(Timestamp createDate) {
        this.createDate = new Date(createDate.getTime());
    }

}
