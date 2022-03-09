package com.hy.demo.Domain.Course.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("/main/*")
public class MainController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping( {"/index"})
    public String index() {

        return  "/main/index";
    }








}
