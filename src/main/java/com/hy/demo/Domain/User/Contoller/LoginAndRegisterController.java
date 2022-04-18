package com.hy.demo.Domain.User.Contoller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;

@Controller
public class LoginAndRegisterController {

    @Autowired
    UserService userService;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping("/login")
    @ResponseBody
    public String login() {


        return "login";
    }


    //login.html ajax return값 보내주는메소드
    @GetMapping("/loginRedirect")
    public @ResponseBody
    String loginRedirect(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (!isEmpty(principalDetails)) {
            logger.info("redirect principalDetails.toString() = " + principalDetails.toString());
            return "main/index";
        }

        return "error";
    }

    @GetMapping("/loginFailRedirect")
    public @ResponseBody
    String loginFailRedirect(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return "error";
    }

    @GetMapping("/userFindPassword")
    public String userFindPassword() {

        return "user/userFindPassword";
    }



    @GetMapping({"/loginForm", "", "/"})
    public String loginForm(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletRequest request) {


        if (!isEmpty(principalDetails)) {
            Boolean check = userService.loginForm(principalDetails.getUser());
            if (check) {
                return "main/index";
            } else {
                logger.info("세션삭제");
                HttpSession session = request.getSession();
                session.invalidate();
                SecurityContextHolder.clearContext();
                return "user/loginForm";
            }


        } else {
            return "user/loginForm";
        }


    }

    @GetMapping("/joinForm")
    public String joinForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {


        if (!isEmpty(principalDetails) && principalDetails.isFlag()) {
            return "main/index";
        } else if (isEmpty(principalDetails)) {
            model.addAttribute("user", null);
            return "user/joinForm";
        } else {
            model.addAttribute("user", principalDetails.getUser());
            return "user/joinForm";
        }


    }

    //세션 업데이트
    private void updateOAuth(Authentication authentication) {
        User findUser = userService.findByUsername(((PrincipalDetails) authentication.getPrincipal()).getUsername());
        PrincipalDetails newPrincipal = new PrincipalDetails(findUser, false);
        UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(newPrincipal,
                        authentication.getCredentials(),
                        newPrincipal.getAuthorities());
        newAuth.setDetails(authentication.getDetails());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }


    @PostMapping("/join")
    public @ResponseBody
    String join(Authentication authentication, User user, @AuthenticationPrincipal PrincipalDetails principalDetails) {//setter 를 쓰지않기위해선 이렇게해야된다.


        logger.info("user.toString() = " + user.toString());
        User provider = null;
        if (!isEmpty(principalDetails)) {
            provider = principalDetails.getUser();
            principalDetails.setFlag(true);
            try {
                userService.register(user, provider);
            } catch (DuplicateKeyException e) {
                String cause = e.getMessage();
                if (cause.equals("아이디")) {
                    return "1";
                } else {
                    return "2";
                }
            }
            updateOAuth(authentication);
            return "main/index";
        } else {
            try {
                userService.register(user, provider);
            } catch (DuplicateKeyException e) {
                String cause = e.getMessage();
                if (cause.equals("아이디")) {
                    return "1";
                } else {
                    return "2";
                }
            }
            return "loginForm";
        }
    }









 /*   @Secured("ROLE_ADMIN")
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

    }*/
}

