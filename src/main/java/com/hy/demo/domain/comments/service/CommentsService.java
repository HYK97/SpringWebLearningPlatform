package com.hy.demo.domain.comments.service;

import com.hy.demo.domain.comments.dto.CommentsDto;
import com.hy.demo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.Map;

public interface CommentsService {
    void createComments(Long courseId, String comments, User user, int status);

    CommentsDto updateComments(Long commentsId, String comments, User user) throws AccessDeniedException;

    CommentsDto createReply(Long commentsId, String comments, User user);

    Page<CommentsDto> findCommentsListByCourseId(Long courseId, Pageable pageable, int status);

    Page<CommentsDto> findReplyListByCommentsId(Long commentsId, Pageable pageable);

    void deleteReply(Long commentsId, User user) throws AccessDeniedException;

    Long countDateCommentCount(Long courseId, String date);

    Map monthlyToDayComments(Long courseId, String date);

    Map thisYearToMonthlyComments(Long courseId, String date);

    Map tenYearToYearComments(Long courseId, String date);
}
