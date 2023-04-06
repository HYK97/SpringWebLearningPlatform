package com.hy.demo.domain.course.repository;

import com.hy.demo.domain.course.dto.CourseDto;
import com.hy.demo.domain.course.entity.Course;
import com.hy.demo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseRepositoryCustom {


    Page<CourseDto> findCourseDtoByCourseName(String courseName, Pageable pageable);

    public List<CourseDto> findByRandomId(int amount);

    public CourseDto findByIdAndUserDTO(Long id);

    Page<CourseDto> findByUserIdAndCourseName(String courseName, Long userId, Pageable pageable);

    Optional<Course> findByUserAndCourseId(User findUser, Long courseId);

    Page<CourseDto> findCourseDtoByCourseNameAndUserId(String courseName, Pageable pageable, Long userId);

    Page<CourseDto> findCourseDtoByUsername(String username, Pageable pageable);

    List<CourseDto> findOrderByScopeAvgCourse(int limit);

    List<CourseDto> findOrderByEvaluationCountCourse(int limit);

    List<CourseDto> findOrderByUserCourse(int limit);


}
