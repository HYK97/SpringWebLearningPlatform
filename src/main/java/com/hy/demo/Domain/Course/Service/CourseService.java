package com.hy.demo.Domain.Course.Service;

import com.hy.demo.Domain.Board.Service.CourseBoardService;
import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseBoardService courseBoardService;

    @Autowired
    private ImageService imageService;

    @Autowired
    Logger logger;
    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;

    public Page<CourseDto> findMyCourseList(Pageable pageable, User user, String search) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername()))
                .orElseThrow(() -> new AccessDeniedException("접근권한없음"));
        Page<CourseDto> results = courseRepository.findCourseDtoByCourseNameAndUserId(search, pageable, findUser.getId());
        return results;
    }


    public Page<CourseDto> findCourseList(Pageable pageable, String search) {
        Page<CourseDto> results = courseRepository.findCourseDtoByCourseName(search, pageable);
        return results;
    }


    public Page<CourseDto> findByAuthorName(Pageable pageable, String authorName) {
        Page<CourseDto> results = courseRepository.findCourseDtoByUsername(authorName, pageable);
        return results;
    }


    public CourseDto findDetailCourse(Long id) {

        CourseDto results = courseRepository.findByIdAndUserDTO(id);//
        //별점갯수
        Map<String, Double> countScope = courseEvaluationRepository.countScope(id);
        Double allCount = 0.0;
        for (String key : countScope.keySet()) {
            allCount += countScope.get(key);
        }
        List<Double> percent = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            percent.add(countScope.get(Integer.toString(i)) / allCount * 100);
        }
        for (Double aDouble : percent) {
            logger.info("aDouble = " + aDouble);
        }
        results.setStarPercent(percent);

        return results;
    }


    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    public Page<CourseDto> findMyCourseList(String search, Long userId, Pageable pageable) {
        Page<CourseDto> results = courseRepository.findByUserIdAndCourseName(search, userId, pageable);
        return results;
    }

    public Course findCourseById(Long id) throws Exception {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new Exception("찾는엔티티없음."));
        return course;
    }

    public List<CourseDto> randomCourseList(int amount) {
        return courseRepository.findByRandomId(amount);
    }

    public List<CourseDto> findRankingScopeAvgCourse(int amount) {
        return courseRepository.findOrderByScopeAvgCourse(amount);
    }

    public List<CourseDto> findRankingEvaluationCountCourse(int amount) {
        return courseRepository.findOrderByEvaluationCountCourse(amount);
    }

    public List<CourseDto> findRankingUserCourseCountCourse(int amount) {
        return courseRepository.findOrderByUserCourse(amount);
    }


    @Transactional
    public void deleteCourse(Long courseId, String username) throws FileNotFoundException {
        User findUser = userRepository.findByUsername(username);
        courseBoardService.courseBoardFileDelete(courseId);
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new AccessDeniedException("권한없음"));
        if (!isEmpty(course.getThumbnail())) {
            try {
                imageService.deleteImage(course.getThumbnail());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new FileNotFoundException("사진 삭제 에러");
            }

            Long result = courseRepository.deleteByIdAndUserId(courseId, findUser.getId());
            if (result == 0L) {
                throw new AccessDeniedException("권한없음");
            }
        }


    }
}
