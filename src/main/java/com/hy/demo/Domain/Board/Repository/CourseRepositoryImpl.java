package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import com.hy.demo.Domain.Board.Entity.QCourse;
import com.hy.demo.Domain.Board.Entity.QCourseEvaluation;
import com.hy.demo.Domain.User.Entity.QUser;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.hy.demo.Domain.Board.Entity.QCourse.course;
import static com.hy.demo.Domain.Board.Entity.QCourseEvaluation.courseEvaluation;
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
        return applyPagination(pageable, query ->
                getCourseDtoJPAQuery()
                        .where(course.courseName.contains(courseName).or(course.teachName.contains(courseName)))
        );
    }

    public CourseDto findByIdAndUserDTO(Long id) {
        return getCourseDtoJPAQuery()
                        .where(course.id.eq(id)).fetchOne();
    }

    private JPAQuery<CourseDto> getCourseDtoJPAQuery() {
        return select(Projections.constructor(CourseDto.class
                , course.id
                , course.courseName
                , user
                , course.createDate
                , course.teachName
                , course.thumbnail
                , course.courseExplanation
                , select(courseEvaluation.scope.avg()).from(courseEvaluation).where(courseEvaluation.course.id.eq(course.id)).groupBy(courseEvaluation.course.id)
                , select(courseEvaluation.scope.count()).from(courseEvaluation).where(courseEvaluation.course.id.eq(course.id)).groupBy(courseEvaluation.course.id)
        ))
                .from(course)
                .leftJoin(course.user, user);
    }



    }

