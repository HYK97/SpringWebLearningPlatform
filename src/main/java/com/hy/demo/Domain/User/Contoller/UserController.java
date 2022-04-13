package com.hy.demo.Domain.User.Contoller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.User.Dto.UserDto;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/user/*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CourseService courseService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping("info")
    public String info(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        UserDto userInfo;
        try {
            userInfo = userService.findUserInfo(principalDetails.getUsername());
        } catch (EntityNotFoundException e) {
            return "403";
        }
        model.addAttribute("user", userInfo);
        return "user/userInfo";
    }


    @GetMapping("userDetailInfo/{username}")
    public String userDetailInfo(@PathVariable String username, Model model, @PageableDefault(size = 6, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        User userInfo;
        try {
            userInfo = userService.findByUsername(username);

        } catch (EntityNotFoundException e) {
            return "403";
        }
        Page<CourseDto> courseDto = courseService.findByAuthorName(pageable, username);
        model.addAttribute("user", userInfo.changeDto());
        pagingDto(model, courseDto);
        return "user/teachInfo";
    }


    @PostMapping("userDetailInfo/{username}")
    @ResponseBody
    public Page<CourseDto> postUserDetailInfo(@PathVariable String username, Model model, @PageableDefault(size = 3, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CourseDto> courseDto = courseService.findByAuthorName(pageable, username);
        return courseDto;
    }

    private <T> void pagingDto(Model model, Page<T> courseDto) {
        List<T> content = courseDto.getContent();
        int pageNumber = courseDto.getPageable().getPageNumber();
        boolean Next = courseDto.hasNext();
        model.addAttribute("course", content);
        model.addAttribute("pageNumber", pageNumber + 1);
        model.addAttribute("Next", Next);
    }


    @PostMapping("update")
    @ResponseBody
    public UserDto update(@AuthenticationPrincipal PrincipalDetails principalDetails, UserDto user) {
        UserDto userDto;
        try {
            userDto = userService.userUpdate(principalDetails.getUser(), user);

        } catch (EntityNotFoundException e) {
            return null;
        }

        return userDto;
    }

    @GetMapping("security")
    public String security() {

        return "user/userSecurity";
    }

    @PostMapping("getUser")
    @ResponseBody
    public UserDto getProfileImage(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User findUser;
        try {
            findUser = userService.findProfileImageByUser(principalDetails.getUser());
        } catch (EntityNotFoundException e) {
            return null;
        }
        return findUser.changeDto();
    }


    @PostMapping("passwordChange")
    @ResponseBody
    public String passwordChange(@AuthenticationPrincipal PrincipalDetails principalDetails, String nowPassword, String newPassword) {

        try {
            userService.passwordUpdate(principalDetails.getUser(), nowPassword, newPassword);
        } catch (EntityNotFoundException e) {
            return "3";
        } catch (AccessDeniedException e) {
            return "2";
        }
        return "1";
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
