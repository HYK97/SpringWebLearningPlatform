package com.hy.demo.Domain.Board.Controller;

import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.SummerNoteImage;
import com.hy.demo.Domain.Board.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    ResourceLoader resourceLoader;

    @PostMapping("/image")
    public ResponseEntity<?> imageUpload( MultipartFile file) {
        try {
            SummerNoteImage uploadFile = imageService.store(file);
            return ResponseEntity.ok().body("/image/" + uploadFile.getId());
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/image/{fileId}")
    public ResponseEntity<?> serveFile(@PathVariable Long fileId){
        try {
            SummerNoteImage uploadFile = imageService.load(fileId);
            Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());
            return ResponseEntity.ok().body(resource);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}
