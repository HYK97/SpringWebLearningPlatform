package com.hy.demo.Domain.Course.Service;

import com.hy.demo.Domain.Course.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import com.hy.demo.Domain.Course.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;

/**
 * service 명명 규칙
 * select -> find
 * modifyCourseEvaluation -> modify
 * insert -> add
 * delete -> delete
 */

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
    public CourseEvaluation addCourseEvaluation(String courseId, String content, String star, User user, String replyId) {
        Long courseLid = Long.parseLong(courseId);
        Course course = courseRepository.findById(courseLid).get();
        if (!isEmpty(course)) { // 해당코스가있을때
            CourseEvaluation build;
            if (isEmpty(star) && !isEmpty(replyId)) { //강사 답변일때
                Long replyLid = Long.parseLong(replyId);
                build = CourseEvaluation.builder().course(course)
                        .comments(content)
                        .replyId(replyLid)
                        .user(user)
                        .build();
            } else { //일반 수강평일때
                CourseEvaluation findCourseEvaluation = courseEvaluationRepository.findByUsernameAndCourseIdAndId(user.getUsername(), courseLid, null);
                if (isEmpty(findCourseEvaluation)) {
                    build = CourseEvaluation.builder().course(course)
                            .comments(content)
                            .scope(Double.valueOf(star))
                            .user(user)
                            .build();
                } else {
                    throw new DataIntegrityViolationException("이미있는 수강평");
                }
            }
            return courseEvaluationRepository.save(build);
        } else {
            throw new IllegalArgumentException("없는 코스임.");
        }
    }


    @Transactional
    public void delete(Long id, User user, Long courseId) {

        CourseEvaluation findCourseEvaluation = courseEvaluationRepository.findByUsernameAndCourseIdAndId(user.getUsername(), courseId, id); //널값뜨면 잘못된 session으로 인한 삭제 방지
        if (!isEmpty(findCourseEvaluation.getReplyId())) { //리플일때
            courseEvaluationRepository.deleteById(findCourseEvaluation.getId());
        } else { //일반댓글일때
            CourseEvaluation replyId = courseEvaluationRepository.findByReplyId(id);
            if (!isEmpty(replyId)) {//댓글이 실제있을때 함께삭제.
                courseEvaluationRepository.deleteById(replyId.getId()); //리플삭제
            }

            logger.info("id = " + id);
            courseEvaluationRepository.deleteById(id);
        }


    }

    public boolean modifyCourseEvaluation(String id, String comments, String star, User user, String courseId) {
        Long courseLid = Long.parseLong(courseId);
        Long Lid = Long.parseLong(id);
        CourseEvaluation courseEvaluation = courseEvaluationRepository.findByUsernameAndCourseIdAndId(user.getUsername(), courseLid, Lid);
        if (!isEmpty(star)) { //일반수강평
            Double doubleStar = Double.valueOf(star);
            if (!isEmpty(courseEvaluation)) {
                courseEvaluation.updateCourseEvaluation(comments, doubleStar);
                courseEvaluationRepository.save(courseEvaluation);
                return true;
            } else {
                return false;
            }
        } else {    // 강사 답글
            if (!isEmpty(courseEvaluation)) {
                courseEvaluation.updateReply(comments);
                courseEvaluationRepository.save(courseEvaluation);
                return true;
            } else {
                return false;
            }
        }

    }

    public boolean countByUserAndCourse(String username, String id) {
        Long Lid = Long.parseLong(id);
        User byUsername = userRepository.findByUsername(username);
        Long aLong = courseEvaluationRepository.countByUserIdAndCourseId(byUsername.getId(), Lid);
        if (aLong == 1) {
            return false;
        } else {
            return true;
        }
    }

    public Double avgDateScope(Long courseId, String date) {
        return courseEvaluationRepository.findDateScopeAvgByCourseId(courseId, date);
    }

    public Map monthlyToDayScopeAvg(Long courseId, String date) {
        return courseEvaluationRepository.findMonthlyToDayScopeAvgByCourseId(courseId, date);
    }

    public Map thisYearToMonthlyScopeAvg(Long courseId, String date) {
        return courseEvaluationRepository.findThisYearToMonthlyScopeAvgByCourseId(courseId, date);
    }

    public Map tenYearToYearScopeAvg(Long courseId, String date) {
        return courseEvaluationRepository.findTenYearToYearScopeAvgByCourseId(courseId, date);
    }
    public Double findAvgScopeByCourseId(Long courseId) {
        return courseEvaluationRepository.findAvgScopeByCourseId(courseId);
    }

}
