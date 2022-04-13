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

    CourseEvaluation findByUsernameAndCourseIdAndId(String username, Long courseId, Long id);

    Double findDateScopeAvgByCourseId(Long courseId, String date);

    Map findMonthlyToDayScopeAvgByCourseId(Long courseId, String date);

    Map findThisYearToMonthlyScopeAvgByCourseId(Long courseId, String date);

    Map findTenYearToYearScopeAvgByCourseId(Long courseId, String date);

    Double findAvgScopeByCourseId(Long courseId);


}
