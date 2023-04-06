package com.hy.demo.domain.comments.controller;


import com.hy.demo.config.auth.PrincipalDetails;
import com.hy.demo.domain.comments.dto.CommentsDto;
import com.hy.demo.domain.comments.service.CommentsService;
import com.hy.demo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class CommentsController {


    private final CommentsService commentsService;


    @PostMapping({"/create/{id}"})
    @ResponseBody
    public String createComments(@PathVariable Long id, @RequestParam @Length(min = 1, max = 400) String comments, @AuthenticationPrincipal PrincipalDetails principalDetails, int status) throws Exception {
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
    public CommentsDto createReply(@PathVariable Long commentsId, @RequestParam @Length(min = 1, max = 400) String comments, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
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
    public CommentsDto updateReply(@PathVariable Long commentsId, @RequestParam @Length(min = 1, max = 400) String comments, @AuthenticationPrincipal PrincipalDetails principalDetails) {
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

