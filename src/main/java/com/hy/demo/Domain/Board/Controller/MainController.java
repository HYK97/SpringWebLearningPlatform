package com.hy.demo.Domain.Board.Controller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;

@Controller

@RequestMapping("/main/*")
public class MainController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping( {"/index"})
    public String index(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (!isEmpty(principalDetails)) {
            return "/main/index";
        }else
            return "/loginForm";
    }








}
