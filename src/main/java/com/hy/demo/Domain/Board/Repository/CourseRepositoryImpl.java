package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.QCourse;
import com.hy.demo.Domain.User.Entity.QUser;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.hy.demo.Domain.Board.Entity.QCourse.course;
import static com.hy.demo.Domain.User.Entity.QUser.user;


public class CourseRepositoryImpl extends QueryDsl4RepositorySupport implements CourseRepositoryCustom  {


    public CourseRepositoryImpl() {
        super(Course.class);
    }

    public Page<Course> findByCourseNameAndUser(String courseName, Pageable pageable) {

        return applyPagination(pageable,query ->
                        query.select(course)
                        .from(course)
                        .leftJoin(course.user, QUser.user)
                        .fetchJoin()
                        .where(course.courseName.contains(courseName))
                    );
    }

    public Page<CourseDto> findByCourseNameAndUserDTO(String courseName, Pageable pageable) {

        return applyPagination(pageable,query ->
                query.select(Projections.constructor(CourseDto.class
                        ,course.id
                        ,course.courseName
                        ,user
                        ,course.heart))
                        .from(course)
                        .leftJoin(course.user, user)
                        .where(course.courseName.contains(courseName))
        );


    }


}
