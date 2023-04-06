package com.hy.demo.domain.file.dto;

import lombok.Data;


@Data
public class FileDto {

    private Long id;

    private Long courseBoardId;

    private Long noticeId;

    private String origFileName;  // 파일 원본명

    private String filePath;  // 파일 저장 경로

    private Long fileSize;

    public FileDto(String origFileName, String filePath, Long fileSize) {
        this.origFileName = origFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public FileDto() {
    }

    public FileDto(Long id, Long courseBoardId, Long noticeId, String origFileName, String filePath, Long fileSize) {
        this.id = id;
        this.courseBoardId = courseBoardId;
        this.noticeId = noticeId;
        this.origFileName = origFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public FileDto(Long id, Long courseBoardId, String origFileName, String filePath, Long fileSize) {
        this.id = id;
        this.courseBoardId = courseBoardId;
        this.origFileName = origFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }


    public FileDto(Long id, String origFileName, Long fileSize) {
        this.id = id;
        this.origFileName = origFileName;
        this.fileSize = fileSize;
    }
}
