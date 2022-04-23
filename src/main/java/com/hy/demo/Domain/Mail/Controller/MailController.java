package com.hy.demo.Domain.Mail.Controller;


import com.hy.demo.Domain.Mail.Service.MailService;
import com.hy.demo.Domain.User.Dto.UserDto;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/mail/*")
public class MailController {
    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;



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
            return "0";// 메일발송 실패
        } catch (EntityNotFoundException e) {
            return "1";// 없는 유저;
        } catch (AccessDeniedException e) {
            return "1";// 잘못된 이메일;
        }
        return "2"; // 메일발송성공

    }

}
