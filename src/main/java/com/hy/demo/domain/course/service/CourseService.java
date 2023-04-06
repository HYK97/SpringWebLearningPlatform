package com.hy.demo.domain.course.service;

import com.hy.demo.domain.course.dto.CourseDto;
import com.hy.demo.domain.course.entity.Course;
import com.hy.demo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.List;

public interface CourseService {
    Page<CourseDto> findMyCourseList(Pageable pageable, User user, String search);

    Page<CourseDto> findCourseList(Pageable pageable, String search);

    Page<CourseDto> findByAuthorName(Pageable pageable, String authorName);

    CourseDto findDetailCourse(Long id);

    void addCourse(Course course);

    Page<CourseDto> findMyCourseList(String search, Long userId, Pageable pageable);

    Course findCourseById(Long id) throws Exception;

    List<CourseDto> randomCourseList(int amount);

    List<CourseDto> findRankingScopeAvgCourse(int amount);

    List<CourseDto> findRankingEvaluationCountCourse(int amount);

    List<CourseDto> findRankingUserCourseCountCourse(int amount);

    @Transactional
    void deleteCourse(Long courseId, String username) throws FileNotFoundException;
}
