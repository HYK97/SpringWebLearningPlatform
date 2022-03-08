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

import javax.transaction.Transactional;
import java.util.Optional;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;

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



    @Transactional
    public CourseEvaluation save(String courseId,String content,String star,User user) {
        Long courseLid= Long.parseLong(courseId);
        Course course = courseRepository.findById(courseLid).get();
        if (!isEmpty(course)) {
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


    @Transactional
    public void delete(Long id,User user,Long courseId) {
        //리플인지 먼저확인

        
        logger.info("id = " + id);
        CourseEvaluation findCourseEvaluation = courseEvaluationRepository.findByUsernameAndId(user.getUsername(),courseId,id); //널값뜨면 잘못된 session으로 인한 삭제 방지
        if (!isEmpty(findCourseEvaluation.getReplyId())) { //리플일때
            courseEvaluationRepository.deleteById(findCourseEvaluation.getId());
        }else { //일반댓글일때
            CourseEvaluation replyId = courseEvaluationRepository.findByReplyId(id);
            if (!isEmpty(replyId)) {//댓글이 실제있을때 함께삭제.
                    courseEvaluationRepository.deleteById(replyId.getId()); //리플삭제
            }
            
            logger.info("id = " + id);
                courseEvaluationRepository.deleteById(id);
        }



    }

    public boolean update(String id,String comments,String star,User user,String courseId) {
        Long courseLid= Long.parseLong(courseId);
        Long Lid = Long.parseLong(id);
        Double doubleStar = Double.valueOf(star);
        CourseEvaluation courseEvaluation= courseEvaluationRepository.findByUsernameAndId(user.getUsername(), courseLid, Lid);
        if (!ObjectUtils.isEmpty(courseEvaluation)) {
            courseEvaluation.updateCourseEvalution(comments, doubleStar);
            courseEvaluationRepository.save(courseEvaluation);
            return true;
        } else {

            return false;
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
