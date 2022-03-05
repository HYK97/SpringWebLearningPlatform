package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface CourseEvaluationRepository extends JpaRepository<CourseEvaluation,Long> ,CourseEvaluationRepositoryCustom {
    Map<String, Double> countScope(Long id);
    Page<CourseEvaluationDto> findByIDCourseEvaluationDTO(Long courseId, Pageable pageable);
    Long countById(Long id);
    CourseEvaluationDto findByReply(Long id);

}
