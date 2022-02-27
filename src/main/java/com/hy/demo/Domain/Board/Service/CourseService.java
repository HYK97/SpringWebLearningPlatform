package com.hy.demo.Domain.Board.Service;

import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Board.Repository.CourseEvaluationRepositoryCustom;
import com.hy.demo.Domain.Board.Repository.CourseRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    Logger logger;
    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;

    public Page<CourseDto> viewCourse(Pageable pageable) {

        Page<CourseDto> results = courseRepository.findByCourseNameAndUserDTO("", pageable);
        return results;
    }

    public CourseDto detailView(Long id) {

        CourseDto results = courseRepository.findByIdAndUserDTO(id);//
        //별점갯수
        Map<String, Double> countScope = courseEvaluationRepository.countScope(id);
        Double allCount = 0.0;
        for (String key : countScope.keySet()) {
            allCount+=countScope.get(key);
        }
        List<Double> percent =new ArrayList<>();
        for (int i = 1; i <6 ; i++) {
            percent.add(countScope.get(Integer.toString(i))/allCount*100);
        }
        for (Double aDouble : percent) {
           logger.info("aDouble = " + aDouble);
        }
        results.setStarPercent(percent);

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
