package com.hy.demo.Domain.Course.Service;

import com.hy.demo.Domain.Course.Entity.SummerNoteImage;
import com.hy.demo.Domain.Course.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;




@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    private final Path rootLocation; // d:/image/

    public ImageService(String uploadPath) {
        this.rootLocation = Paths.get(uploadPath);
        System.out.println(rootLocation.toString());
    }

    public SummerNoteImage store(MultipartFile file) throws Exception { //db
        try {
            if(file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }
            String saveFileName = fileSave(rootLocation.toString(), file);
            SummerNoteImage saveImage = SummerNoteImage.builder()
                    .fileName(file.getOriginalFilename())
                    .saveFileName(saveFileName)
                    .contentType(file.getContentType())
                    .size(file.getResource().contentLength())
                    .filePath(rootLocation.toString().replace(File.separatorChar, '/') + '/' + saveFileName)
                    .build();
            imageRepository.save(saveImage);
            return saveImage;

        } catch(IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }


    }




    public SummerNoteImage load(Long fileId) {
        return imageRepository.findById(fileId).get();
    }

    private String fileSave(String rootLocation, MultipartFile file) throws IOException { //disk
        File uploadDir = new File(rootLocation);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // saveFileName 생성
        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid.toString() + file.getOriginalFilename();
        File saveFile = new File(rootLocation, saveFileName);
        FileCopyUtils.copy(file.getBytes(), saveFile);

        return saveFileName;
    }
}