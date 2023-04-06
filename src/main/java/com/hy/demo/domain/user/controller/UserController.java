package com.hy.demo.domain.user.controller;

import com.hy.demo.config.auth.PrincipalDetails;
import com.hy.demo.domain.course.dto.CourseDto;
import com.hy.demo.domain.course.entity.SummerNoteImage;
import com.hy.demo.domain.course.service.CourseService;
import com.hy.demo.domain.course.service.ImageService;
import com.hy.demo.domain.user.dto.UserDto;
import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.service.UserService;
import com.hy.demo.domain.user.form.UserUpdateForm;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.hy.demo.enumcode.AJAXResponseCode.*;

@Controller
@RequestMapping("/user/*")
@RequiredArgsConstructor
@Validated
public class UserController {


    private final UserService userService;
    private final CourseService courseService;

    private final ImageService imageService;

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
    public String userDetailInfo(@PathVariable @Length(min = 1, max = 200, message = "1자 이상 200자 이하으로 작성해주세요") String username, Model model, @PageableDefault(size = 6, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
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
    public Page<CourseDto> postUserDetailInfo(@PathVariable @Length(min = 1, max = 200, message = "1자 이상 200자 이하으로 작성해주세요") String username, @PageableDefault(size = 3, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
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
    public UserDto update(@AuthenticationPrincipal PrincipalDetails principalDetails, @ModelAttribute @Validated UserUpdateForm form) {
        UserDto userDto = form.toDto();
        try {

            userDto = userService.userUpdate(principalDetails.getUser(), userDto);

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


    @PostMapping("profileUpdate")
    @ResponseBody
    public String profileUpdate(MultipartFile file, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            User findUser = userService.findByUsername(principalDetails.getUsername());
            imageService.deleteImage(findUser.getProfileImage());
            SummerNoteImage uploadFile = imageService.store(file);
            userService.updateUserProfileImage("/image/" + uploadFile.getId(), principalDetails.getUser());
            return "/image/" + uploadFile.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("passwordChange")
    @ResponseBody
    public String passwordChange(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                 @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
                                 @Length(min = 1, max = 200, message = "1자 이상 200자 이하로 작성하세요 ")
                                 String nowPassword,
                                 @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
                                 @Length(min = 1, max = 200, message = "1자 이상 200자 이하로 작성하세요 ")
                                 String newPassword) {
        try {
            userService.passwordUpdate(principalDetails.getUser(), nowPassword, newPassword);
        } catch (EntityNotFoundException e) {
            return ERROR.toString();
        } catch (AccessDeniedException e) {
            return FAIL.toString();
        }
        return OK.toString();
    }

    @PostMapping("courseWithdrawal/{courseId}")
    @ResponseBody
    public String courseWithdrawal(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long courseId) {
        try {
            userService.courseWithdrawal(principalDetails.getUsername(), courseId);
        } catch (AccessDeniedException e) {
            return FAIL.toString(); //삭제실패
        }
        return OK.toString(); //삭제성공
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
