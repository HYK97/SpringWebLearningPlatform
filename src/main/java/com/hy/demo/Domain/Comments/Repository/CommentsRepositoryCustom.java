package com.hy.demo.Domain.Comments.Repository;

import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.Comments.Entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface CommentsRepositoryCustom {
    Page<CommentsDto> findByCourseBoardId(Long courseBoardId, Pageable pageable);

    Page<CommentsDto> findReplyByIds(Long id, Pageable pageable);

    Long countDateCommentCountByCourseId(Long courseId, String date);

    Optional<Comments> findByIdAndUser(Long id, String username);


    Map countMonthlyToDayCommentsByCourseId(Long courseId, String date) ;

    Map countThisYearToMonthlyCommentsByCourseId(Long courseId, String date);

    Map countTenYearToYearCommentsByCourseId(Long courseId, String date);
}
