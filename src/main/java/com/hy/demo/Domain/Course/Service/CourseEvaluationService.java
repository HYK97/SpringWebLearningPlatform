package com.hy.demo.Domain.Course.Service;

import com.hy.demo.Domain.Course.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import com.hy.demo.Domain.User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.Map;

public interface CourseEvaluationService {
    Page<CourseEvaluationDto> courseEvaluationView(Long id, Pageable pageable);

    @Transactional
    CourseEvaluation addCourseEvaluation(String courseId, String content, String star, User user, String replyId);

    @Transactional
    void delete(Long id, User user, Long courseId);

    boolean modifyCourseEvaluation(String id, String comments, String star, User user, String courseId);

    boolean countByUserAndCourse(String username, String id);

    Double avgDateScope(Long courseId, String date);

    Map monthlyToDayScopeAvg(Long courseId, String date);

    Map thisYearToMonthlyScopeAvg(Long courseId, String date);

    Map tenYearToYearScopeAvg(Long courseId, String date);

    Double findAvgScopeByCourseId(Long courseId);
}
