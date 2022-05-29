package com.hy.demo.Domain.Board.Service;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.File.Dto.FileDto;
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
