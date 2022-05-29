package com.hy.demo.Domain.Course.Service;

import com.hy.demo.Domain.Course.Entity.SummerNoteImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface ImageService {
    Boolean deleteImage(String id) throws FileNotFoundException;

    SummerNoteImage store(MultipartFile file) throws Exception;

    SummerNoteImage load(Long fileId);
}
