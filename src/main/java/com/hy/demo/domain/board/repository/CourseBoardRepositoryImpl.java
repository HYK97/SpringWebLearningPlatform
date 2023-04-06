package com.hy.demo.domain.board.repository;

import com.hy.demo.domain.board.dto.CourseBoardDto;
import com.hy.demo.domain.board.entity.CourseBoard;
import com.hy.demo.utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;

import java.util.List;
import java.util.Optional;

import static com.hy.demo.domain.board.entity.QCourseBoard.courseBoard;
import static com.hy.demo.domain.comments.entity.QComments.comments1;
import static com.hy.demo.domain.course.entity.QCourse.course;
import static com.hy.demo.domain.file.entity.QFile.file;


public class CourseBoardRepositoryImpl extends QueryDsl4RepositorySupport implements CourseBoardRepositoryCustom {

    public CourseBoardRepositoryImpl() {
        super(CourseBoard.class);
    }


    @Override
    public List<CourseBoardDto> findByCourseIdNotContents(Long courseId) {
        return select(Projections.constructor(CourseBoardDto.class,
                courseBoard.id,
                courseBoard.title,
                courseBoard.views,
                courseBoard.createDate,
                courseBoard.contents,
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
                .orderBy(comments1.count().desc())
                .fetch();
    }


}
