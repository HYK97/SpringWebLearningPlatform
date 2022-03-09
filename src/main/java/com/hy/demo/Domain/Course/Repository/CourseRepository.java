package com.hy.demo.Domain.Course.Repository;

import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long>,CourseRepositoryCustom {

    //findBy규칙 -> Username문법



    Page<Course> findByCourseNameAndUser(String CourseName, Pageable pageable);
    Page<CourseDto> findByCourseNameAndUserDTO(String courseName, Pageable pageable) ;
    CourseDto findByIdAndUserDTO(Long id);
    Course findByCourseName(String coursename);
    void deleteById(Long id);
}
