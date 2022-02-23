package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CourseRepository extends JpaRepository<Course,Long>,CourseRepositoryCustom {

    //findBy규칙 -> Username문법



    Page<Course> findByCourseNameAndUser(String CourseName, Pageable pageable);
    Page<CourseDto> findByCourseNameAndUserDTO(String courseName, Pageable pageable) ;
    void deleteById(Long id);
}
