package com.hy.demo.domain.course.repository;

import com.hy.demo.domain.course.dto.CourseDto;
import com.hy.demo.domain.course.entity.Course;
import com.hy.demo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseRepositoryCustom {

    //findBy규칙 -> Username문법


    Page<CourseDto> findCourseDtoByCourseName(String courseName, Pageable pageable);

    CourseDto findByIdAndUserDTO(Long id);

    Course findByCourseName(String search);

    public List<CourseDto> findByRandomId(int amount);

    Optional<Course> findByUserAndId(User user, Long id);

    void deleteById(Long id);

    Page<CourseDto> findByUserIdAndCourseName(String search, Long userId, Pageable pageable);

    Optional<Course> findByUserAndCourseId(User findUser, Long courseId);

    Page<CourseDto> findCourseDtoByCourseNameAndUserId(String search, Pageable pageable, Long userId);

    Page<CourseDto> findCourseDtoByUsername(String username, Pageable pageable);

    List<CourseDto> findOrderByScopeAvgCourse(int limit);

    List<CourseDto> findOrderByEvaluationCountCourse(int limit);

    List<CourseDto> findOrderByUserCourse(int limit);

    Long deleteByIdAndUserId(Long id, Long userId);

}
