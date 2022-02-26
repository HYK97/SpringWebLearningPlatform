package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseEvaluationRepository extends JpaRepository<CourseEvaluation,Long> {

}
