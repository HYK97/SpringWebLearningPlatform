package com.hy.demo.domain.course.service;

import com.hy.demo.domain.course.entity.SummerNoteImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface ImageService {
    Boolean deleteImage(String id) throws FileNotFoundException;

    SummerNoteImage store(MultipartFile file) throws Exception;

    SummerNoteImage load(Long fileId);
}
