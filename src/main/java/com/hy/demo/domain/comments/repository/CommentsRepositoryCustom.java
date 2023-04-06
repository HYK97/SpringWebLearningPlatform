package com.hy.demo.domain.comments.repository;

import com.hy.demo.domain.comments.dto.CommentsDto;
import com.hy.demo.domain.comments.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface CommentsRepositoryCustom {
    Page<CommentsDto> findByCourseBoardId(Long courseBoardId, Pageable pageable, int status);

    Page<CommentsDto> findReplyByIds(Long id, Pageable pageable);

    Long countDateCommentCountByCourseId(Long courseId, String date);

    Optional<Comments> findByIdAndUser(Long id, String username);


    Map countMonthlyToDayCommentsByCourseId(Long courseId, String date);

    Map countThisYearToMonthlyCommentsByCourseId(Long courseId, String date);

    Map countTenYearToYearCommentsByCourseId(Long courseId, String date);
}
