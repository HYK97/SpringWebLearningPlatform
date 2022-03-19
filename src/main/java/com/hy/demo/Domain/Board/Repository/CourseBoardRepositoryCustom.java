package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;

import java.util.List;
import java.util.Optional;

public interface CourseBoardRepositoryCustom {
    public List<CourseBoardDto> findByCourseIdNotContents(Long courseId);
    Optional<List<CourseBoard>> findByCourseId(Long courseId);
}
