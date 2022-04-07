package com.hy.demo.Domain.Course.Repository;

import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseRepositoryCustom {

    public Page<Course> findByCourseNameAndUser(String CourseName, Pageable pageable);

    Page<CourseDto> findCourseDtoByCourseName(String courseName, Pageable pageable);

    public List<CourseDto> findByRandomId(int amount);

    public CourseDto findByIdAndUserDTO(Long id);

    Page<CourseDto> findByUserIdAndCourseName(String courseName, Long userId, Pageable pageable);

    Optional<Course> findByUserAndCourseId(User findUser, Long courseId);
}
