package com.hy.demo.domain.course.controller;

import com.hy.demo.domain.course.entity.SummerNoteImage;
import com.hy.demo.domain.course.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequiredArgsConstructor
public class ImageController {


    private final ImageService imageService;


    private final ResourceLoader resourceLoader;


    @PostMapping("/image")
    public ResponseEntity<?> imageUpload(MultipartFile file) {
        try {
            SummerNoteImage uploadFile = imageService.store(file);
            return ResponseEntity.ok().body("/image/" + uploadFile.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/image/{fileId}")
    public ResponseEntity<?> serveFile(@PathVariable Long fileId) {
        try {
            SummerNoteImage uploadFile = imageService.load(fileId);
            Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());
            return ResponseEntity.ok().body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}