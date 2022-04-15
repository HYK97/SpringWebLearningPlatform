package com.hy.demo.Domain.Comments.Controller;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Service.CourseBoardService;
import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.Comments.Entity.Comments;
import com.hy.demo.Domain.Comments.Service.CommentsService;
import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Service.FileService;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Service.UserService;
import com.hy.demo.Utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

;

@Controller
@RequestMapping("/comments/*")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private Logger logger;


    @PostMapping({"/create/{id}"})
    @ResponseBody
    public String createComments(@PathVariable Long id, String comments, @AuthenticationPrincipal PrincipalDetails principalDetails, int status) throws Exception {
        User user = principalDetails.getUser();
        try {
            commentsService.createComments(id, comments, user, status);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return "2";
        }
        return "1";
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
            return "0";
        }
        return "1";
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

