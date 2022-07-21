package com.hy.demo.Domain.Comments.Controller;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.Comments.Service.CommentsService;
import com.hy.demo.Domain.User.Entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.nio.file.AccessDeniedException;

import static com.hy.demo.enumcode.AJAXResponseCode.FAIL;
import static com.hy.demo.enumcode.AJAXResponseCode.OK;

;

@Controller
@RequestMapping("/comments/*")
@RequiredArgsConstructor
public class CommentsController {


    private final CommentsService commentsService;


    @PostMapping({"/create/{id}"})
    @ResponseBody
    public String createComments(@PathVariable Long id, String comments, @AuthenticationPrincipal PrincipalDetails principalDetails, int status, Request request) throws Exception {
        User user = principalDetails.getUser();
        try {
            commentsService.createComments(id, comments, user, status);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return FAIL.toString();
        }
        return OK.toString();
    }

    @PostMapping({"/createReply/{commentsId}"})
    @ResponseBody
    public CommentsDto createReply(@PathVariable Long commentsId, String comments, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        User user = principalDetails.getUser();
        CommentsDto reply;
        try {
            reply = commentsService.createReply(commentsId, comments, user);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return reply;
    }


    @PostMapping({"/updateReply/{commentsId}"})
    @ResponseBody
    public CommentsDto updateReply(@PathVariable Long commentsId, String comments, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        CommentsDto reply;
        try {
            reply = commentsService.updateComments(commentsId, comments, user);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            return null;
        }
        return reply;
    }


    @PostMapping({"/deleteReply/{commentsId}"})
    @ResponseBody
    public String deleteReply(@PathVariable Long commentsId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        try {
            commentsService.deleteReply(commentsId, user);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            return FAIL.toString();
        }
        return OK.toString();
    }


    @GetMapping({"/getComments/{id}"})
    @ResponseBody
    public Page<CommentsDto> getComments(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, int status) {
        String username = principalDetails.getUser().getUsername();
        Page<CommentsDto> commentsList;
        try {
            commentsList = commentsService.findCommentsListByCourseId(id, pageable, status);
            commentsList.stream().filter(f -> f.getUser().getUsername().equals(username)).forEach(f -> f.setMyCommentsFlag(1));
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
        return commentsList;
    }

    @GetMapping({"/getReply/{id}"})
    @ResponseBody
    public Page<CommentsDto> getReply(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.ASC) Pageable pageable) {
        String username = principalDetails.getUser().getUsername();
        Page<CommentsDto> replyList;
        try {
            replyList = commentsService.findReplyListByCommentsId(id, pageable);
            replyList.stream().filter(f -> f.getUser().getUsername().equals(username)).forEach(f -> f.setMyCommentsFlag(1));
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
        return replyList;
    }


}

