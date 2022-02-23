package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Repository.support.QueryDsl4RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.hy.demo.Domain.Board.Entity.QCourse.*;
import static com.hy.demo.Domain.User.Entity.QUser.*;


public class CourseRepositoryImpl extends QueryDsl4RepositorySupport implements CourseRepositoryCustom  {


    public CourseRepositoryImpl() {
        super(Course.class);
    }

    public Page<Course> findByCourseNameAndUser(String courseName, Pageable pageable) {

        return applyPagination(pageable,query ->
                        query.select(course)
                        .from(course)
                        .leftJoin(course.user, user)
                        .fetchJoin()
                        .where(course.courseName.contains(courseName))
                    );


    }


}
