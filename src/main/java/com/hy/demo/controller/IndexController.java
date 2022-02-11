package com.hy.demo.controller;

import com.hy.demo.Entity.User;
import com.hy.demo.Repository.UserRepository;
import com.hy.demo.config.auth.PrincipalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class IndexController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;



    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }


    //OAuth 로그인을해도 principalDetails
    //일반로그인도 principalDetails
    @ResponseBody
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        logger.info("principalDetails.getUser() = " + principalDetails.getUser());
        logger.info(principalDetails.getAttributes()==null ? "일반회원입니다.": "OAuth 외부 회원입니다.");

        return "user";
    }

    @ResponseBody
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @ResponseBody
    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @ResponseBody
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }


    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        logger.info("user.toString() = " + user.toString());

        userRepository.save(user); // 비밀번호가 암호화 되지않으면 시큐리티로 로그인할수없음.
        return "redirect:/loginForm";
    }


    //일반 로그인
    @GetMapping("/test/login")
    @ResponseBody
    public String loginTest(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails) {
        //첫번째 방법
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        logger.info("authentication.getPrincipal() = " + principalDetails.getUser());
        //두번째 방법 두번째는 @AuthenticationPrincipal를 사용해야함
        logger.info("userDetails.getUsername() = " + userDetails.getUser());
        return "세션정보 확인하기";
    }

    //oauth 로그인
    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String loginOAuthTest(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oAuthUser) {
        //다운 캐스팅작업 첫번째 방법
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        logger.info("authentication.getPrincipal() = " + oAuth2User.getAttributes());

        // 두번째방법
        logger.info("oAuthUser = " + oAuthUser.getAttributes());


        return "oauth세션정보 확인하기";
    }


    @Secured("ROLE_ADMIN")
    @ResponseBody
    @GetMapping("/info")
    public String info(){

        return "개인정보";

    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/data")
    public String data(){

        return "개인정보";

    }
}
