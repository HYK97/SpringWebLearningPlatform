package com.hy.demo.Domain.User.Contoller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.User.Dto.UserDto;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/user/*")
public class UserController {

    @Autowired
    UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping("info")
    public String info(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        UserDto userInfo;
        try {
            userInfo = userService.findUserInfo(principalDetails.getUser());

        } catch (EntityNotFoundException e) {
            return "403";
        }
        model.addAttribute("user", userInfo);
        return "/user/userInfo";
    }

    @PostMapping("update")
    @ResponseBody
    public UserDto update(@AuthenticationPrincipal PrincipalDetails principalDetails, String email) {
        UserDto userDto;
        try {
            userDto = userService.userUpdate(principalDetails.getUser(), email);

        } catch (EntityNotFoundException e) {
            return null;
        }

        return userDto;
    }

    @GetMapping("security")
    public String security(@AuthenticationPrincipal PrincipalDetails principalDetails, Authentication authentication) {

        return "/user/userSecurity";
    }

    @PostMapping("passwordChange")
    @ResponseBody
    public String passwordChange(@AuthenticationPrincipal PrincipalDetails principalDetails, String nowPassword,String newPassword) {

        try {
            userService.passwordUpdate(principalDetails.getUser(), nowPassword,newPassword);
        } catch (EntityNotFoundException e) {
            return "3";
        } catch (AccessDeniedException e) {
            return "2";
        }
        return "1";
    }

    @ResponseBody
    @PostMapping("role")
    public String session(Authentication authentication) {

        return ((PrincipalDetails) authentication.getPrincipal()).getUser().getRole();
    }


    //테스트용
    @ResponseBody
    @GetMapping("admin")
    public String admin() {
        return "admin";
    }

    @ResponseBody
    @GetMapping("manager")
    public String manager() {
        return "manager";
    }


}
