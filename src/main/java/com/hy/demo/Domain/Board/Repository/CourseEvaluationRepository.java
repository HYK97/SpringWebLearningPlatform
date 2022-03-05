package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface CourseEvaluationRepository extends JpaRepository<CourseEvaluation,Long> ,CourseEvaluationRepositoryCustom {
    Map<String, Double> countScope(Long id);
    Page<CourseEvaluation> findByIDCourseEvaluationDTO(Long courseId, Pageable pageable);
    Long countById(Long id);

}
