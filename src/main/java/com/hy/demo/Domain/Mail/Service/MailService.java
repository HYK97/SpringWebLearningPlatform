package com.hy.demo.Domain.Mail.Service;

import com.hy.demo.Domain.Mail.Dto.MailDto;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public void sendPasswordMail(String username) throws MessagingException {
        User user = Optional.ofNullable(userRepository.findByUsername(username)).orElseThrow(() -> new EntityNotFoundException("없는 유저"));
        String tempPassword = tempPassword();
        MimeMessage message = null;

        //메일 객체 생성
        MailDto mail = new MailDto();
        mail.setAddress(user.getEmail());
        mail.setTitle("akaSpring 임시 비밀번호 발송");
        mail.setMessage(user.getUsername() + "님 임시 비밀번호입니다 : " + tempPassword + "<br> " + "<a href='https://www.akaspringplatform.p-e.kr/user/security'>임시비밀번호변경</a> ");

        //페스워드 변경
        user.updatePassword(bCryptPasswordEncoder.encode(tempPassword));
        userRepository.save(user);

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

