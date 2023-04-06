package com.hy.demo.domain.file.service;

import com.hy.demo.domain.file.dto.FileDto;
import com.hy.demo.domain.file.entity.File;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileService {
    List<FileDto> localSaveFile(List<MultipartFile> multipartFile) throws IOException;

    void saveFile(List<File> files);

    Map<String, Object> fileDownLoad(Long fileId) throws IOException;

    boolean deleteFile(List<File> files);

    boolean deleteFileDto(List<FileDto> files);
}
