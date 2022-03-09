package com.hy.demo.Domain.Course.Repository;

import com.hy.demo.Domain.Course.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CourseEvaluationRepositoryCustom {
    Map<String, Double> countScope(Long id);
    CourseEvaluation findByReply(Long id);
    Page<CourseEvaluationDto> findByIDCourseEvaluationDTO(Long courseId, Pageable pageable);
    CourseEvaluation findByUsernameAndId(String username,Long courseId,Long id);
}
