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
import java.util.ArrayList;
import java.util.List;

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
    Logger logger;

    public List<CourseBoardDto> findCourseBoardList(Long id) {
        return courseBoardRepository.findByCourseIdNotContents(id);
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




}
