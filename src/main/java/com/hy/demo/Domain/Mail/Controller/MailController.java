package com.hy.demo.Domain.Mail.Controller;


import com.hy.demo.Domain.Mail.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("send")
    @ResponseBody
    public String send(String username) {

        try {
            mailService.sendPasswordMail(username);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "0";// 메일발송 실패
        } catch (EntityNotFoundException e) {
            return "1";// 없는 유저;
        }
        return "2"; // 메일발송성공

    }

}
