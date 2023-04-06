package com.hy.demo.domain.mail.controller;


import com.hy.demo.domain.mail.service.MailServiceImpl;
import com.hy.demo.domain.user.dto.UserDto;
import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;

import static com.hy.demo.enumcode.AJAXResponseCode.*;

@Controller
@RequestMapping("/mail/*")
@RequiredArgsConstructor
public class MailController {

    private final MailServiceImpl mailService;

    private final UserService userService;


    @PostMapping("send")
    @ResponseBody
    public String send(UserDto user) {


        try {
            //유저정보 확인
            User findUser = userService.findEmailAndUsername(user);
            //비동기 메일보내기
            mailService.sendMail(findUser);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ERROR.toString();// 메일발송 실패
        } catch (EntityNotFoundException e) {
            return FAIL.toString();// 없는 유저;
        } catch (AccessDeniedException e) {
            return FAIL.toString();// 잘못된 이메일;
        }
        return OK.toString(); // 메일발송성공

    }

}
