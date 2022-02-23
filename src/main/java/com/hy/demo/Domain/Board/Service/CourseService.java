package com.hy.demo.Domain.Board.Service;

import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> viewCourse(Pageable pageable) {
        Page<Course> results = courseRepository.findByCourseNameAndUser("", pageable);
        return results.getContent();
    }

    public List<Course> searchCourse(String search, Pageable pageable) {
        Page<Course> results = courseRepository.findByCourseNameAndUser(search, pageable);
        return results.getContent();
    }

    public void createCourse(Course course) {

        courseRepository.save(course);
    }

}
