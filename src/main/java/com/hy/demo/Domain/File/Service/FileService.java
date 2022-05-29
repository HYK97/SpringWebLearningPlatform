package com.hy.demo.Domain.File.Service;

import com.hy.demo.Domain.File.Dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileService {
    List<FileDto> localSaveFile(List<MultipartFile> multipartFile) throws IOException;

    void saveFile(List<com.hy.demo.Domain.File.Entity.File> files);

    Map<String, Object> fileDownLoad(Long fileId) throws IOException;

    boolean deleteFile(List<com.hy.demo.Domain.File.Entity.File> files);

    boolean deleteFileDto(List<FileDto> files);
}
