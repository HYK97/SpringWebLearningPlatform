package com.hy.demo.domain.board.repository;

import com.hy.demo.domain.board.dto.CourseBoardDto;
import com.hy.demo.domain.board.entity.CourseBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseBoardRepository extends JpaRepository<CourseBoard, Long>, CourseBoardRepositoryCustom {

    List<CourseBoardDto> findByCourseIdNotContents(Long courseId);

    Optional<List<CourseBoard>> findByCourseId(Long courseId);

    Optional<CourseBoard> findById(Long courseBoardId);

    CourseBoard findByCourseBoardId(Long courseBoardId);

    Long countViewByCourseId(Long courseId);

    List<CourseBoardDto> findRankViewByCourseId(Long courseId);

}
