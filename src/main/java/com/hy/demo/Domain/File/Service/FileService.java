package com.hy.demo.Domain.File.Service;


import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Repository.FileRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;


@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    public List<FileDto> localSaveFile(List<MultipartFile> multipartFile) throws IOException {
        LocalDate now = LocalDate.now();
        String Path = "resources/upload/" + now;
        File fileDir = new File(Path);
        List<FileDto> files = new ArrayList<>();
        //license 폴더가 없으면 생성
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        try {
            if (multipartFile.size() > 0 && !multipartFile.get(0).getOriginalFilename().equals("")) {
                for (MultipartFile file : multipartFile) {
                    String originalFileName = file.getOriginalFilename();    //오리지날 파일명
                    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));    //파일 확장자
                    String savedFileName = UUID.randomUUID() + extension;    //저장될 파일 명
                    Long fileSize = file.getSize();
                    File targetFile = new File(Path + savedFileName);
                    try {
                        InputStream fileStream = file.getInputStream();
                        FileUtils.copyInputStreamToFile(fileStream, targetFile); //파일 저장
                        FileDto fileDto = new FileDto(originalFileName, targetFile.getPath(), fileSize);
                        files.add(fileDto);
                    } catch (Exception e) {
                        //파일삭제
                        FileUtils.deleteQuietly(targetFile);    //저장된 현재 파일 삭제
                        e.printStackTrace();
                        break;
                    }
                }
                return files;
            } else
                return files;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IOException("오류");
    }


    public void saveFile(List<com.hy.demo.Domain.File.Entity.File> files) {
        fileRepository.saveAll(files);
    }

    public Map<String,Object> fileDownLoad(Long fileId) throws IOException {

        com.hy.demo.Domain.File.Entity.File findFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("찾는파일없음"));
        Path filePath = Paths.get(findFile.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
        Map map =new HashMap();
        map.put("resource",resource);
        map.put("file",new File(findFile.getFilePath()));
        map.put("fileName",findFile.getOrigFileName());
        return map;
    }


}