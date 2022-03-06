package com.hy.demo.Domain.Board.Service;

import com.hy.demo.Domain.Board.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Board.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Board.Repository.CourseRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseEvaluationService {
    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;

    @Autowired
    Logger logger;

    public Page<CourseEvaluationDto>  courseEvaluationView(Long id, Pageable pageable) {
       return courseEvaluationRepository.findByIDCourseEvaluationDTO(id,pageable);
    }





}
