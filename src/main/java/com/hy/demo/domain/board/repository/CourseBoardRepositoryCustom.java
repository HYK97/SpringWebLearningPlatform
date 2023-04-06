package com.hy.demo.domain.board.repository;

import com.hy.demo.domain.board.dto.CourseBoardDto;
import com.hy.demo.domain.board.entity.CourseBoard;

import java.util.List;
import java.util.Optional;

public interface CourseBoardRepositoryCustom {
    public List<CourseBoardDto> findByCourseIdNotContents(Long courseId);

    Optional<List<CourseBoard>> findByCourseId(Long courseId);

    CourseBoard findByCourseBoardId(Long courseBoardId);

    Long countViewByCourseId(Long courseId);

    public List<CourseBoardDto> findRankViewByCourseId(Long courseId);

}
