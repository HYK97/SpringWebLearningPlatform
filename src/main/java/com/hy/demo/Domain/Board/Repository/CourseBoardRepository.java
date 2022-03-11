package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseBoardRepository extends JpaRepository<CourseBoard, Long>, CourseBoardRepositoryCustom {

    public List<CourseBoardDto> findByCourseIdNotContents(Long courseId);
}