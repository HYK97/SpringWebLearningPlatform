package com.hy.demo.domain.course.service;

import com.hy.demo.domain.board.service.CourseBoardService;
import com.hy.demo.domain.course.dto.CourseDto;
import com.hy.demo.domain.course.entity.Course;
import com.hy.demo.domain.course.repository.CourseEvaluationRepository;
import com.hy.demo.domain.course.repository.CourseRepository;
import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static com.hy.demo.utils.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {


    private final CourseRepository courseRepository;


    private final UserRepository userRepository;


    private final CourseBoardService courseBoardService;


    private final ImageServiceImpl imageService;


    private final CourseEvaluationRepository courseEvaluationRepository;

    @Override
    public Page<CourseDto> findMyCourseList(Pageable pageable, User user, String search) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername()))
                .orElseThrow(() -> new AccessDeniedException("접근권한없음"));
        Page<CourseDto> results = courseRepository.findCourseDtoByCourseNameAndUserId(search, pageable, findUser.getId());
        return results;
    }


    @Override
    public Page<CourseDto> findCourseList(Pageable pageable, String search) {
        Page<CourseDto> results = courseRepository.findCourseDtoByCourseName(search, pageable);
        return results;
    }


    @Override
    public Page<CourseDto> findByAuthorName(Pageable pageable, String authorName) {
        Page<CourseDto> results = courseRepository.findCourseDtoByUsername(authorName, pageable);
        return results;
    }


    @Override
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
            log.debug("aDouble = {}", aDouble);
        }
        results.setStarPercent(percent);

        return results;
    }


    @Override
    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    @Override
    public Page<CourseDto> findMyCourseList(String search, Long userId, Pageable pageable) {
        Page<CourseDto> results = courseRepository.findByUserIdAndCourseName(search, userId, pageable);
        return results;
    }

    @Override
    public Course findCourseById(Long id) throws Exception {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new Exception("찾는엔티티없음."));
        return course;
    }

    @Override
    public List<CourseDto> randomCourseList(int amount) {
        return courseRepository.findByRandomId(amount);
    }

    @Override
    public List<CourseDto> findRankingScopeAvgCourse(int amount) {
        return courseRepository.findOrderByScopeAvgCourse(amount);
    }

    @Override
    public List<CourseDto> findRankingEvaluationCountCourse(int amount) {
        return courseRepository.findOrderByEvaluationCountCourse(amount);
    }

    @Override
    public List<CourseDto> findRankingUserCourseCountCourse(int amount) {
        return courseRepository.findOrderByUserCourse(amount);
    }


    @Override
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

        }
        Long result = courseRepository.deleteByIdAndUserId(courseId, findUser.getId());
        if (result == 0L) {
            throw new AccessDeniedException("권한없음");
        }


    }
}
