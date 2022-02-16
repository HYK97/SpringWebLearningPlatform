package com.hy.demo.Domain.User.Contoller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Utils.ObjectUtils;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginAndRegisterController {

    @Autowired
    UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @PostMapping("/login")
    public String login(User user,@AuthenticationPrincipal PrincipalDetails principalDetails,HttpServletRequest request,String username) {


        return "redirect:/";
    }


    @GetMapping("/loginForm")
    public String loginForm(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (!ObjectUtils.isEmpty(principalDetails)) {
            return "redirect:/";
        }


        return "/user/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {


        if (!ObjectUtils.isEmpty(principalDetails)&&principalDetails.isFlag()) {
            return "redirect:/";
        }else if(ObjectUtils.isEmpty(principalDetails)) {
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
        if (!ObjectUtils.isEmpty(principalDetails)) {
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
