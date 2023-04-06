package com.hy.demo.domain.comments.service;

import com.hy.demo.domain.board.entity.CourseBoard;
import com.hy.demo.domain.board.repository.CourseBoardRepository;
import com.hy.demo.domain.comments.dto.CommentsDto;
import com.hy.demo.domain.comments.entity.Comments;
import com.hy.demo.domain.comments.repository.CommentsRepository;
import com.hy.demo.domain.community.entity.Community;
import com.hy.demo.domain.community.repository.CommunityRepository;
import com.hy.demo.domain.user.dto.UserDto;
import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class CommentsServiceImpl implements CommentsService {

    private final CourseBoardRepository courseBoardRepository;


    private final CommentsRepository commentsRepository;


    private final CommunityRepository communityRepository;


    private final UserRepository userRepository;


    @Override
    public void createComments(Long courseId, String comments, User user, int status) {
        Comments comment;
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername()))
                .orElseThrow(() -> new EntityNotFoundException("찾는 유저 없음"));
        if (status == 1) {
            CourseBoard courseBoard = courseBoardRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("찾는 courseBoard없음"));
            comment = Comments.builder()
                    .comments(comments)
                    .user(findUser)
                    .courseBoard(courseBoard)
                    .build();
        } else {
            Community community = communityRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("찾는 courseBoard없음"));
            comment = Comments.builder()
                    .comments(comments)
                    .user(findUser)
                    .community(community)
                    .build();
        }


        commentsRepository.save(comment);
    }


    @Override
    public CommentsDto updateComments(Long commentsId, String comments, User user) throws AccessDeniedException {
        Comments findComments = commentsRepository.findByIdAndUser(commentsId, user.getUsername()).orElseThrow(() -> new AccessDeniedException("권한없음"));
        findComments.updateComments(comments);
        Comments comment = commentsRepository.save(findComments);
        return comment.changeDto();

    }


    @Override
    public CommentsDto createReply(Long commentsId, String comments, User user) {


        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername()))
                .orElseThrow(() -> new EntityNotFoundException("찾는 유저 없음"));

        Comments comment = commentsRepository.findById(commentsId)
                .orElseThrow(() -> new EntityNotFoundException("찾는 댓글없음"));


        UserDto userDto = new UserDto();
        userDto.setUsername(findUser.getUsername());
        userDto.setNickname(findUser.getNickname());
        userDto.setProfileImage(findUser.getProfileImage());
        Comments reply = Comments.builder()
                .parent(comment)
                .comments(comments)
                .user(findUser)
                .courseBoard(comment.getCourseBoard())
                .community(comment.getCommunity())
                .build();
        Comments replyEntity = commentsRepository.save(reply);
        CommentsDto commentsDto = replyEntity.changeDto();
        commentsDto.setMyCommentsFlag(1);
        commentsDto.setReplyId(comment.getId());
        commentsDto.setUser(userDto);
        return commentsDto;
    }


    @Override
    public Page<CommentsDto> findCommentsListByCourseId(Long courseId, Pageable pageable, int status) {
        Page<CommentsDto> commentsList = commentsRepository.findByCourseBoardId(courseId, pageable, status);
        for (CommentsDto commentsDto : commentsList.getContent()) {
            log.debug("commentsList = {}", commentsDto.toString());
        }
        return commentsList;
    }


    @Override
    public Page<CommentsDto> findReplyListByCommentsId(Long commentsId, Pageable pageable) {
        Page<CommentsDto> replyByIds = commentsRepository.findReplyByIds(commentsId, pageable);
        return replyByIds;
    }

    @Override
    public void deleteReply(Long commentsId, User user) throws AccessDeniedException {
        Comments findComments = commentsRepository.findByIdAndUser(commentsId, user.getUsername()).orElseThrow(() -> new AccessDeniedException("권한없음"));
        commentsRepository.delete(findComments);
    }

    @Override
    public Long countDateCommentCount(Long courseId, String date) {
        return commentsRepository.countDateCommentCountByCourseId(courseId, date);
    }

    @Override
    public Map monthlyToDayComments(Long courseId, String date) {
        return commentsRepository.countMonthlyToDayCommentsByCourseId(courseId, date);
    }

    @Override
    public Map thisYearToMonthlyComments(Long courseId, String date) {
        return commentsRepository.countThisYearToMonthlyCommentsByCourseId(courseId, date);
    }

    @Override
    public Map tenYearToYearComments(Long courseId, String date) {
        return commentsRepository.countTenYearToYearCommentsByCourseId(courseId, date);
    }


}
