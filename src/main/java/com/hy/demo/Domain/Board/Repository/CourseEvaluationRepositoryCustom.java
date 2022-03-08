package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CourseEvaluationRepositoryCustom {
    Map<String, Double> countScope(Long id);
    CourseEvaluation findByReply(Long id);
    Page<CourseEvaluationDto> findByIDCourseEvaluationDTO(Long courseId, Pageable pageable);
    CourseEvaluation findByUsernameAndId(String username,Long courseId,Long id);
}
