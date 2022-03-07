package com.hy.demo.Domain.Board.Service;

import com.hy.demo.Domain.Board.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import com.hy.demo.Domain.Board.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Board.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import com.hy.demo.Utils.ObjectUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseEvaluationService {
    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    Logger logger;

    public Page<CourseEvaluationDto> courseEvaluationView(Long id, Pageable pageable) {
        return courseEvaluationRepository.findByIDCourseEvaluationDTO(id, pageable);
    }


    public CourseEvaluation save(String courseId,String content,String star,User user) {
        Long courseLid= Long.parseLong(courseId);
        Course course = courseRepository.findById(courseLid).get();
        if (!ObjectUtils.isEmpty(course)) {
            CourseEvaluation build = CourseEvaluation.builder().course(course)
                    .comments(content)
                    .scope(Double.valueOf(star))
                    .user(user)
                    .build();
            return courseEvaluationRepository.save(build);
        } else {
            throw new IllegalArgumentException("없는 코스임.");
        }


    }

    public boolean countByUserAndCourse(String username,String id) {

        Long Lid= Long.parseLong(id);
        User byUsername = userRepository.findByUsername(username);
        Long aLong = courseEvaluationRepository.countByUserIdAndCourseId(byUsername.getId(), Lid);
        if (aLong == 1) {
            return false;
        } else {
            return true;
        }
    }



}
