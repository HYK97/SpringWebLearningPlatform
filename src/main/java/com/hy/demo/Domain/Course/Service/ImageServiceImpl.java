package com.hy.demo.Domain.Course.Service;

import com.hy.demo.Domain.Course.Entity.SummerNoteImage;
import com.hy.demo.Domain.Course.Repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;


@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {


    private final ImageRepository imageRepository;


    private final String uploadPath;


    @Override
    public Boolean deleteImage(String id) throws FileNotFoundException {
        if (!isEmpty(id)) {
            String[] split = id.split("/");
            Long imageId = Long.valueOf(split[2]);
            SummerNoteImage load = load(imageId);
            String filePath = load.getFilePath();
            log.debug("path = {}", filePath);
            File newFile = new File(filePath);
            imageRepository.deleteById(imageId);
            if (newFile.exists()) {
                if (newFile.delete()) {
                } else {
                    throw new FileNotFoundException("실패");
                }
            } else {
                throw new FileNotFoundException("실패");
            }
            return true;
        } else {
            return true;
        }
    }

    @Override
    public SummerNoteImage store(MultipartFile file) throws Exception { //db
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }
            String saveFileName = fileSave(uploadPath, file);
            SummerNoteImage saveImage = SummerNoteImage.builder()
                    .fileName(file.getOriginalFilename())
                    .saveFileName(saveFileName)
                    .contentType(file.getContentType())
                    .size(file.getResource().contentLength())
                    .filePath(uploadPath.replace(File.separatorChar, '/') + '/' + saveFileName)
                    .build();
            imageRepository.save(saveImage);
            return saveImage;

        } catch (IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }


    }


    @Override
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