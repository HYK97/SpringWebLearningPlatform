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
public class LoginAndRegister {

    @Autowired
    UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @PostMapping("/login")
    public String login(User user,@AuthenticationPrincipal PrincipalDetails principalDetails,HttpServletRequest request,String username) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);

        if (!ObjectUtils.isEmpty(principalDetails)) {
            return "redirect:/";
        }

        return "login";
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

        //세션 비어있지 않음 즉 로그인 상태 , 일반로그인은 provider 비어있음 얘는 루트로 보내야댐 auth는 비어있지않음.예는 회원가입 해야됨
        

        if (!ObjectUtils.isEmpty(principalDetails)&&principalDetails.isFlag()) {

            return "redirect:/";
        }

        if (ObjectUtils.isEmpty(principalDetails)) {
            model.addAttribute("user",null);
        }else{
            model.addAttribute("user", principalDetails.getUser());

        }


        return "/user/joinForm";
    }


    @PostMapping("/join")
    public String join(User user, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model, HttpServletResponse response) {//setter 를 쓰지않기위해선 이렇게해야된다.



        User provider=null;
        if (!ObjectUtils.isEmpty(principalDetails)) {
        provider =principalDetails.getUser();
        principalDetails.setFlag(true);
        }


        userService.register(user,provider);
        return "redirect:/loginForm";
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
