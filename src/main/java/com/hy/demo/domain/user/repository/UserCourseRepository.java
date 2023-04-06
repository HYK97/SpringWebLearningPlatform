package com.hy.demo.domain.user.repository;

import com.hy.demo.domain.course.entity.Course;
import com.hy.demo.domain.user.dto.UserDto;
import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {


    UserCourse findByUserAndCourse(User user, Course course);

    Optional<UserCourse> findByUserAndCourseId(User user, Long courseId);

    Long countDateRegisteredUserCountByCourseId(Long courseId, String date);

    Map countMonthlyToDayRegisteredUserByCourseId(Long courseId, String date);

    Map countThisYearToMonthlyRegisteredUserByCourseId(Long courseId, String date);

    Map countTenYearToYearRegisteredUserByCourseId(Long courseId, String date);

    void nativeQuery(Long courseId);

    List<UserDto> findRankRandomUserById(int amount);

    Long deleteByCourseIdAndUserId(Long courseId, Long userId);

}
