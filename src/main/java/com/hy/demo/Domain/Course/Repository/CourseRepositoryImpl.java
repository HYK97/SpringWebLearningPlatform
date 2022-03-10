package com.hy.demo.Domain.Course.Repository;

import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.User.Entity.QUser;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.hy.demo.Domain.Course.Entity.QCourse.course;
import static com.hy.demo.Domain.Course.Entity.QCourseEvaluation.courseEvaluation;
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


    public List<CourseDto> findByRandomId(List<Long> id) {
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
                .leftJoin(course.user, user)
                .where(course.id.in(id))
                .fetch();
    }



    }

