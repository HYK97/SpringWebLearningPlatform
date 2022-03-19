package com.hy.demo.Domain.Course.Repository;

import com.hy.demo.Domain.Board.Entity.QCourseBoard;
import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.User.Entity.QUser;
import com.hy.demo.Domain.User.Entity.QUserCourse;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.hy.demo.Domain.Board.Entity.QCourseBoard.courseBoard;
import static com.hy.demo.Domain.Course.Entity.QCourse.course;
import static com.hy.demo.Domain.Course.Entity.QCourseEvaluation.courseEvaluation;
import static com.hy.demo.Domain.User.Entity.QUser.user;
import static com.hy.demo.Domain.User.Entity.QUserCourse.*;
import static com.querydsl.core.types.ExpressionUtils.count;


public class CourseRepositoryImpl extends QueryDsl4RepositorySupport implements CourseRepositoryCustom {


    @Autowired
    Logger logger;

    public CourseRepositoryImpl() {
        super(Course.class);
    }

    public Page<Course> findByCourseNameAndUser(String courseName, Pageable pageable) {

        return applyPagination(pageable, query ->
                query.select(course)
                        .from(course)
                        .leftJoin(course.user, QUser.user)
                        .fetchJoin()
                        .where(course.courseName.contains(courseName))
        );
    }


    public Page<CourseDto> findByUserIdAndCourseName(String courseName, Long userId, Pageable pageable) {
        return applyPagination(pageable, query -> select(Projections.constructor(CourseDto.class
                , course.id
                , course.courseName
                , course.createDate
                , course.teachName
                , select(courseEvaluation.scope.avg().as("scope")).from(courseEvaluation).where(courseEvaluation.course.id.eq(course.id)).groupBy(courseEvaluation.course.id)
                , select(userCourse.course.count().as("userJoinCount")).from(userCourse).where(userCourse.course.id.eq(course.id)).groupBy(userCourse.course.id)
                ))
                        .from(course)
                        .leftJoin(course.user, user)
                        .where(userIdeEq(userId), courseNameContains(courseName))
        );
    }

    @Override
    public Optional<Course> findByUserAndCourseId(User findUser,Long courseId) {
        return Optional.ofNullable(
                select(course)
                .from(course)
                .where(user.user.eq(findUser).and(course.id.eq(courseId)))
                .fetchOne()
        );
    }

    private BooleanExpression userIdeEq(Long userId) {
        return userId != null ? course.user.id.eq(userId) : null;
    }

    private BooleanExpression courseNameContains(String courseName) {
        return courseName != null ? course.courseName.contains(courseName) : null;
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
        return getSelectConstructorCourseDto()
                .from(course)
                .leftJoin(course.user, user);
    }


    public List<CourseDto> findByRandomId(int amount) {
        return getSelectConstructorCourseDto()
                .from(course)
                .leftJoin(course.user, user)
                .orderBy(NumberExpression.random().asc())
                .limit(amount)
                .fetch();
    }

    private JPAQuery<CourseDto> getSelectConstructorCourseDto() {
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
        ));
    }


}

