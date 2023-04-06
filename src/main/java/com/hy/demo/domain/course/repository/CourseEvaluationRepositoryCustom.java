package com.hy.demo.domain.course.repository;

import com.hy.demo.domain.course.dto.CourseEvaluationDto;
import com.hy.demo.domain.course.entity.CourseEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CourseEvaluationRepositoryCustom {
    Map<String, Double> countScope(Long id);

    CourseEvaluation findByReply(Long id);

    Page<CourseEvaluationDto> findByIDCourseEvaluationDTO(Long courseId, Pageable pageable);

    CourseEvaluation findByUsernameAndCourseIdAndId(String username, Long courseId, Long id);

    Double findDateScopeAvgByCourseId(Long courseId, String date);

    Map findMonthlyToDayScopeAvgByCourseId(Long courseId, String date);

    Map findThisYearToMonthlyScopeAvgByCourseId(Long courseId, String date);

    Map findTenYearToYearScopeAvgByCourseId(Long courseId, String date);

    Double findAvgScopeByCourseId(Long courseId);


}
