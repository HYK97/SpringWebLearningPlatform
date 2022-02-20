package com.hy.demo.Domain.Board.Controller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.User.Service.UserService;
import com.hy.demo.Utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;

@Controller

@RequestMapping("/main/*")
public class MainController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping( {"/index"})
    public String index(Authentication authentication) {

        if (!isEmpty(authentication)) {
            return "/main/index";
        }else
            return "/loginForm";
    }

}
