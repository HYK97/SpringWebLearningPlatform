package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.hy.demo.Domain.Board.Entity.QCourseBoard.courseBoard;
import static com.hy.demo.Domain.Course.Entity.QCourse.course;


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
                .leftJoin(courseBoard.course,course)
                .where(courseBoard.course.id.eq(courseId))
                .fetch();
    }
}
