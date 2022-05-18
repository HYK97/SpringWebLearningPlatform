package com.hy.demo.Domain.Mail.Service;

import com.hy.demo.Domain.Mail.Dto.MailDto;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("async")
@RequiredArgsConstructor
public class MailService {


    private final JavaMailSender mailSender;


    private final UserRepository userRepository;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Async("executor")
    public void sendMail(User findUser) throws MessagingException {
        String tempPassword = tempPassword();
        MimeMessage message = null;

        //메일 객체 생성
        MailDto mail = new MailDto();
        mail.setAddress(findUser.getEmail());
        mail.setTitle("akaSpring 임시 비밀번호 발송");
        mail.setMessage(findUser.getUsername() + "님 임시 비밀번호입니다 : " + tempPassword + "<br> " + "<a href='https://www.akaspringplatform.p-e.kr/user/security'>임시비밀번호변경</a> ");

        //페스워드 변경
        findUser.updatePassword(bCryptPasswordEncoder.encode(tempPassword));
        userRepository.save(findUser);
        message = mailSender.createMimeMessage();
        MimeMessageHelper mailHelper = new MimeMessageHelper(message, "UTF-8");
        mailHelper.setFrom("스프링강의사이트<ddh963963@gmail.com>");
        mailHelper.setTo(mail.getAddress());
        mailHelper.setSubject(mail.getTitle());
        mailHelper.setText(mail.getMessage(), true);
        mailSender.send(message);
        mailSender.send(message);
    }

    private String tempPassword() {
        String password = "";
        for (int i = 0; i < 10; i++) {
            password += (char) ((Math.random() * 26) + 97);
        }
        return password;
    }


}

