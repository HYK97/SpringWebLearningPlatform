package com.hy.demo.Domain.Course.Repository;

import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.User.Entity.User;
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

}
