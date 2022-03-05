package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CourseEvaluationRepositoryCustom {
    public Map<String, Double> countScope(Long id);

    Page<CourseEvaluationDto> findByIDCourseEvaluationDTO(Long courseId, Pageable pageable);
}
