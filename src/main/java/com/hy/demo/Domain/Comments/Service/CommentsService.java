package com.hy.demo.Domain.Comments.Service;

import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Repository.CourseBoardRepository;
import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.Comments.Entity.Comments;
import com.hy.demo.Domain.Comments.Repository.CommentsRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Optional;

/**
 * service 명명 규칙
 * select -> find
 * modifyCourseEvaluation -> modify
 * insert -> add
 * delete -> delete
 */

@Service
public class CommentsService {
    @Autowired
    private CourseBoardRepository courseBoardRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    Logger logger;

    public void createComments(Long courseId, String comments, User user) {
        CourseBoard courseBoard = courseBoardRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("찾는 courseBoard없음"));

        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername()))
                .orElseThrow(() -> new EntityNotFoundException("찾는 유저 없음"));

        Comments comment = Comments.builder()
                .comments(comments)
                .user(findUser)
                .courseBoard(courseBoard)
                .build();
        commentsRepository.save(comment);
    }


    public CommentsDto updateComments(Long commentsId, String comments, User user) throws AccessDeniedException {
        Comments findComments = commentsRepository.findByIdAndUser(commentsId, user.getUsername()).orElseThrow(() -> new AccessDeniedException("권한없음"));
        findComments.updateComments(comments);
        Comments comment = commentsRepository.save(findComments);
        return comment.changeDto();

    }


    public CommentsDto createReply(Long commentsId, Long courseId, String comments, User user) {
        CourseBoard courseBoard = courseBoardRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("찾는 courseBoard없음"));

        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername()))
                .orElseThrow(() -> new EntityNotFoundException("찾는 유저 없음"));

        Comments comment = commentsRepository.findById(commentsId)
                .orElseThrow(() -> new EntityNotFoundException("찾는 댓글없음"));

        Comments reply = Comments.builder()
                .parent(comment)
                .comments(comments)
                .user(findUser)
                .courseBoard(courseBoard)
                .build();
        Comments replyEntity = commentsRepository.save(reply);
        CommentsDto commentsDto = replyEntity.changeDto();
        commentsDto.setMyCommentsFlag(1);
        commentsDto.setReplyId(comment.getId());
        commentsDto.setUsername(findUser.getUsername());
        commentsDto.setProfileImage(findUser.getProfileImage());
        return commentsDto;
    }


    public Page<CommentsDto> findCommentsListByCourseId(Long courseId, Pageable pageable) {
        Page<CommentsDto> commentsList = commentsRepository.findByCourseBoardId(courseId, pageable);
        for (CommentsDto commentsDto : commentsList.getContent()) {
            logger.info("commentsList = " + commentsDto.toString());
        }
        return commentsList;
    }

    public Page<CommentsDto> findReplyListByCommentsId(Long commentsId, Pageable pageable) {
        Page<CommentsDto> replyByIds = commentsRepository.findReplyByIds(commentsId, pageable);
        return replyByIds;
    }

    public void deleteReply(Long commentsId, User user) throws AccessDeniedException {
        Comments findComments = commentsRepository.findByIdAndUser(commentsId, user.getUsername()).orElseThrow(() -> new AccessDeniedException("권한없음"));
        commentsRepository.delete(findComments);
    }

    public Long countDateCommentCount(Long courseId, String date) {
        return commentsRepository.countDateCommentCountByCourseId(courseId, date);
    }
    public Map monthlyToDayComments(Long courseId, String date) {
        return commentsRepository.countMonthlyToDayCommentsByCourseId(courseId, date);
    }
    public Map thisYearToMonthlyComments(Long courseId, String date) {
        return commentsRepository.countThisYearToMonthlyCommentsByCourseId(courseId, date);
    }
    public Map tenYearToYearComments(Long courseId, String date) {
        return commentsRepository.countTenYearToYearCommentsByCourseId(courseId, date);
    }



}
