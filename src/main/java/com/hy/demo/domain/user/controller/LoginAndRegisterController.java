package com.hy.demo.domain.user.controller;

import com.hy.demo.config.auth.PrincipalDetails;
import com.hy.demo.domain.user.dto.UserDto;
import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.service.UserService;
import com.hy.demo.domain.user.form.UserJoinForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hy.demo.utils.ObjectUtils.isEmpty;
import static com.hy.demo.enumcode.AJAXResponseCode.ERROR;
import static com.hy.demo.enumcode.AJAXResponseCode.FAIL;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginAndRegisterController {


    private final UserService userService;


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
            log.debug("redirect principalDetails.toString() = {}", principalDetails.toString());
            return "main/index";
        }

        return "error";
    }

    @GetMapping("/loginFailRedirect")
    public @ResponseBody
    String loginFailRedirect() {

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
                log.debug("세션삭제");
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
    @ResponseBody
    public Object join(Authentication authentication, @Validated UserJoinForm userForm, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {//setter 를 쓰지않기위해선 이렇게해야된다.

        if (bindingResult.hasErrors()) {
            Map<String, String> validationErrors = bindingResult.getFieldErrors()
                    .stream().collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage
                    ));
            if (!validationErrors.isEmpty()) {
                return validationErrors;
            }
        }
        UserDto userDto = userForm.toDto();
        log.debug("user.toString() = {}", userForm.toString());
        User provider = null;
        if (!isEmpty(principalDetails)) {
            provider = principalDetails.getUser();
            principalDetails.setFlag(true);
            try {
                userService.register(userDto, provider);
            } catch (DuplicateKeyException e) {
                String cause = e.getMessage();
                if (cause.equals("아이디")) {
                    //아이디 존재
                    return FAIL.toString();
                } else {
                    //닉네임 존재
                    return ERROR.toString();
                }
            }
            updateOAuth(authentication);
            return "main/index";
        } else {
            try {
                userService.register(userDto, provider);
            } catch (DuplicateKeyException e) {
                String cause = e.getMessage();
                if (cause.equals("아이디")) {
                    return FAIL.toString();
                } else {
                    return ERROR.toString();
                }
            }
            return "loginForm";
        }
    }


}

