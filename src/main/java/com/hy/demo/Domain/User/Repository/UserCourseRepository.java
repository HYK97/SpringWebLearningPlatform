package com.hy.demo.Domain.User.Repository;

import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCourseRepository extends JpaRepository<UserCourse,Long> {


    public UserCourse findByUserAndCourse(User user , Course course);

}
