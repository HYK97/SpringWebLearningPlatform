package com.hy.demo.Domain.Course.Service;

import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    Logger logger;
    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;

    public Page<CourseDto> findCourseList(Pageable pageable) {
        Page<CourseDto> results = courseRepository.findByCourseNameAndUserDTO("", pageable);
        return results;
    }

    public CourseDto findDetailCourse(Long id) {

        CourseDto results = courseRepository.findByIdAndUserDTO(id);//
        //별점갯수
        Map<String, Double> countScope = courseEvaluationRepository.countScope(id);
        Double allCount = 0.0;
        for (String key : countScope.keySet()) {
            allCount+=countScope.get(key);
        }
        List<Double> percent =new ArrayList<>();
        for (int i = 5; i >0 ; i--) {
            percent.add(countScope.get(Integer.toString(i))/allCount*100);
        }
        for (Double aDouble : percent) {
           logger.info("aDouble = " + aDouble);
        }
        results.setStarPercent(percent);

        return results;
    }


    public Page<CourseDto> findSearchCourseList(String search, Pageable pageable) {
        Page<CourseDto> results = courseRepository.findByCourseNameAndUserDTO(search, pageable);
        return results;
    }

    public void addCourse(Course course) {
        courseRepository.save(course);
    }



    public List<CourseDto> randomCourseList(int amount) {
        List<Long> randomId = new ArrayList<Long>();
        Long id = null;
        Long count = courseRepository.count();
        if (amount > count) {
            throw new IllegalStateException("너무 큰범위");
        }
        while (true) {
            id = ThreadLocalRandom.current().nextLong(1, count+1);
            boolean contains = randomId.contains(id);
            if (!contains) {
                randomId.add(id);
                if (amount == randomId.size()) {
                    break;
                }
            }
        }
        return courseRepository.findByRandomId(randomId);
    }

}
