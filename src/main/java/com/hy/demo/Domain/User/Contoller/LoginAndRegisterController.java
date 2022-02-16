package com.hy.demo.Domain.User.Contoller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Utils.ObjectUtils;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.hy.demo.Utils.ObjectUtils.*;

@Controller
public class LoginAndRegisterController {

    @Autowired
    UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping("/login")
    public @ResponseBody String login(User user,@AuthenticationPrincipal PrincipalDetails principalDetails,HttpServletRequest request,String username) {


        return "/login";
    }

    @PostMapping("/joinFails")
    public @ResponseBody String joinFails(String data,HttpServletRequest request) {

        logger.info("data = " + data);
        if (data.equals("1")) {
            return "/";
        } else {
            logger.info("세션삭제");
            HttpSession session = request.getSession();
            session.invalidate();
            SecurityContextHolder.clearContext();
            return "/loginForm";
        }


    }


    @GetMapping("/loginForm")
    public String loginForm(@AuthenticationPrincipal PrincipalDetails principalDetails) {
            

        if (!isEmpty(principalDetails)) {
            logger.info("principalDetails.toString() = " + principalDetails.toString());
            return "redirect:/";

        }


        return "/user/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {


        if (!isEmpty(principalDetails)&&principalDetails.isFlag()) {
            return "redirect:/";
        }else if(isEmpty(principalDetails)) {
            model.addAttribute("user",null);
            return "/user/joinForm";
        }else{
            model.addAttribute("user", principalDetails.getUser());
            return "/user/joinForm";
        }


    }


    @PostMapping("/join")
    public @ResponseBody String join(User user, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model, HttpServletResponse response) {//setter 를 쓰지않기위해선 이렇게해야된다.



        User provider=null;
        if (!isEmpty(principalDetails)) {
        provider =principalDetails.getUser();
        principalDetails.setFlag(true);
        }

        userService.register(user,provider);
        return "/loginForm";
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
