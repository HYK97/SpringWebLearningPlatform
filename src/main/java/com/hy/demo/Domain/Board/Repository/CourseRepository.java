package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CourseRepository extends JpaRepository<Course,Integer>,CourseRepositoryCustom {

    //findBy규칙 -> Username문법



    List<Course> findByCourseName(String courseName);
    Course deleteById(Long id);
}
