package com.hy.demo.Domain.Board.Service;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Repository.CourseBoardRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    Logger logger;

    public List<CourseBoardDto> findCourseBoardList(Long id) {
        return courseBoardRepository.findByCourseIdNotContents(id);
    }

    public void save(CourseBoard courseBoard) {
        courseBoardRepository.save(courseBoard);
    }




}
