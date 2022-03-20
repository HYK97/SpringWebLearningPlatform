package com.hy.demo.Domain.Board.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Repository.CourseBoardRepository;
import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Entity.File;
import com.hy.demo.Domain.File.Repository.FileRepository;
import com.hy.demo.Domain.File.Service.FileService;
import com.hy.demo.Utils.ObjectUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * service 명명 규칙
 * select -> find
 * modifyCourseEvaluation -> modify
 * insert -> add
 * delete -> delete
 * */

@Service
public class CourseBoardService {
    @Autowired
    private CourseBoardRepository courseBoardRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileService fileService;



    @Autowired
    Logger logger;

    @Transactional
    public List<CourseBoardDto> findCourseBoardList(Long id) {
        List<CourseBoardDto> courseBoardDtoList = courseBoardRepository.findByCourseIdNotContents(id);
        for (CourseBoardDto courseBoardDto : courseBoardDtoList) {
            List<FileDto> findFiles = fileRepository.findFileIdByCourseId(courseBoardDto.getId());
            courseBoardDto.setFiles(findFiles);
        }
        return courseBoardDtoList;
    }

    @Transactional
    public void save(CourseBoard courseBoard,List<FileDto> fileDto) {
        List<File> files=new ArrayList<>();
        CourseBoard findCourseBoard = null;
        if (fileDto.size() == 0) {
            courseBoardRepository.save(courseBoard);
        } else {
            findCourseBoard = courseBoardRepository.save(courseBoard);
            for (FileDto dto : fileDto) {
                File file =File.builder()
                        .courseBoard(findCourseBoard)
                        .filePath(dto.getFilePath())
                        .origFileName(dto.getOrigFileName())
                        .fileSize(dto.getFileSize())
                        .build();
                files.add(file);

            }
            fileRepository.saveAll(files);
        }
    }

    public void deleteBoardAndFiles(Long courseBoardId) throws AccessDeniedException {
        
        //코스확인
        CourseBoard courseBoard = courseBoardRepository.findById(courseBoardId)
                .orElseThrow(() -> new AccessDeniedException("없는코스"));
        Optional<List<File>> findFiles = fileRepository.findByCourseBoardId(courseBoardId);
        List<File> files = findFiles.orElseGet(null);
        if (!ObjectUtils.isEmpty(files)) {
            fileService.deleteFile(files);
        }
        courseBoardRepository.delete(courseBoard);
    }




}
