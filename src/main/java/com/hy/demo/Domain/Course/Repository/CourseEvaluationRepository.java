package com.hy.demo.Domain.Course.Repository;

import com.hy.demo.Domain.Course.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface CourseEvaluationRepository extends JpaRepository<CourseEvaluation,Long> ,CourseEvaluationRepositoryCustom {
    Map<String, Double> countScope(Long id);
    Page<CourseEvaluationDto> findByIDCourseEvaluationDTO(Long courseId, Pageable pageable);
    Long countById(Long id);
    CourseEvaluation findByReply(Long id);
    Long countByUserIdAndCourseId(Long userId, Long courseId);
    CourseEvaluation findByReplyId(Long id);
    CourseEvaluation findByUsernameAndCourseIdAndId(String username, Long courseId, Long id);
}
