package com.hy.demo.Domain.Board.Service;

import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Page<CourseDto> viewCourse(Pageable pageable) {

        Page<CourseDto> results = courseRepository.findByCourseNameAndUserDTO("", pageable);
        return results;
    }

    public Page<CourseDto> searchCourse(String search, Pageable pageable) {
        Page<CourseDto> results = courseRepository.findByCourseNameAndUserDTO(search, pageable);
        return results;
    }

    public void createCourse(Course course) {

        courseRepository.save(course);
    }

}
