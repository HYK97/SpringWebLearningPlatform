package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;

import java.util.List;

public interface CourseBoardRepositoryCustom {
    public List<CourseBoardDto> findByCourseIdNotContents(Long courseId);
}
