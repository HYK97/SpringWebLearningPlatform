package com.hy.demo.Domain.User.Repository;

import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

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


}
