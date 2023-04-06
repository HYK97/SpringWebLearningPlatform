package com.hy.demo.domain.board.service;

import com.hy.demo.domain.board.dto.CourseBoardDto;
import com.hy.demo.domain.board.entity.CourseBoard;
import com.hy.demo.domain.file.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CourseBoardService {


    void updateCourseBoard(Long id, List<MultipartFile> file, String title, String contents) throws Exception;


    List<CourseBoardDto> findCourseBoardList(Long id);


    CourseBoardDto findCourseBoardByCourseBoardId(Long courseBoardId);

    void viewPlus(Long id);


    void save(CourseBoard courseBoard, List<FileDto> fileDto);

    void deleteBoardAndFiles(Long courseBoardId) throws AccessDeniedException;


    void updateBoard(CourseBoard courseBoard, List<FileDto> fileDto);

    Long countAllView(Long courseId);

    List<CourseBoardDto> rankView(Long courseId);

    void courseBoardFileDelete(Long courseId);
}
