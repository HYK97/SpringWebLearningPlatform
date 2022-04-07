package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.hy.demo.Domain.Board.Entity.QCourseBoard.courseBoard;
import static com.hy.demo.Domain.Comments.Entity.QComments.comments1;
import static com.hy.demo.Domain.Course.Entity.QCourse.course;
import static com.hy.demo.Domain.File.Entity.QFile.file;


public class CourseBoardRepositoryImpl extends QueryDsl4RepositorySupport implements CourseBoardRepositoryCustom {


    public CourseBoardRepositoryImpl() {
        super(CourseBoard.class);
    }


    @Autowired
    Logger logger;


    @Override
    public List<CourseBoardDto> findByCourseIdNotContents(Long courseId) {
        return select(Projections.constructor(CourseBoardDto.class,
                courseBoard.id,
                courseBoard.title,
                courseBoard.views,
                courseBoard.createDate,
                courseBoard.contents,
                course.teachName,
                course.courseName))
                .from(courseBoard)
                .leftJoin(courseBoard.course, course)
                .where(courseBoard.course.id.eq(courseId))
                .fetch();
    }


    @Override
    public Optional<List<CourseBoard>> findByCourseId(Long courseId) {
        return Optional.ofNullable(
                selectFrom(courseBoard)
                        .where(courseBoard.course.id.eq(courseId))
                        .fetch()
        );
    }

    public CourseBoard findByCourseBoardId(Long courseBoardId) {
        return select(courseBoard)
                .from(courseBoard)
                .leftJoin(courseBoard.course, course)
                .fetchJoin()
                .leftJoin(courseBoard.files, file)
                .fetchJoin()
                .where(courseBoard.id.eq(courseBoardId))
                .fetchOne();
    }

    public Long countViewByCourseId(Long courseId) {
        return select(courseBoard.views.sum())
                .from(courseBoard)
                .leftJoin(courseBoard.course, course)
                .where(course.id.eq(courseId))
                .fetchOne();
    }


    public List<CourseBoardDto> findRankViewByCourseId(Long courseId) {
        return select(Projections.constructor(CourseBoardDto.class,
                courseBoard.id,
                courseBoard.title,
                courseBoard.views,
                comments1.count()
        ))
                .from(courseBoard)
                .leftJoin(courseBoard.course, course)
                .leftJoin(courseBoard.comments, comments1)
                .where(courseBoard.course.id.eq(courseId))
                .groupBy(courseBoard)
                .orderBy(comments1.count().asc())
                .fetch();
    }


}
