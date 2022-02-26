package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepositoryCustom {

    public Page<Course> findByCourseNameAndUser(String CourseName, Pageable pageable);
    public Page<CourseDto> findByCourseNameAndUserDTO(String courseName, Pageable pageable);
    Page<CourseEvaluation> findByIDCourseEvaluationDTO(Long courseId, Pageable pageable);
    public CourseDto findByIdAndUserDTO(Long id);
}
