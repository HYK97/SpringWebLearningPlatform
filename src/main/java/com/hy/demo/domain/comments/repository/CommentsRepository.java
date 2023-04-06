package com.hy.demo.domain.comments.repository;

import com.hy.demo.domain.comments.dto.CommentsDto;
import com.hy.demo.domain.comments.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments, Long>, CommentsRepositoryCustom {
    Page<CommentsDto> findByCourseBoardId(Long courseBoardId, Pageable pageable, int status);

    Page<CommentsDto> findReplyByIds(Long id, Pageable pageable);

    Optional<Comments> findByIdAndUser(Long id, String username);

    Long countDateCommentCountByCourseId(Long courseId, String date);

    Map countMonthlyToDayCommentsByCourseId(Long courseId, String date);

    Map countThisYearToMonthlyCommentsByCourseId(Long courseId, String date);

    Map countTenYearToYearCommentsByCourseId(Long courseId, String date);
}
