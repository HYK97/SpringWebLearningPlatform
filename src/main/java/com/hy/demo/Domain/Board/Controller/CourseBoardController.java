package com.hy.demo.Domain.Board.Controller;


import com.hy.demo.Domain.Board.Service.CourseBoardService;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.Course.Service.ImageService;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

;

@Controller
@RequestMapping("/courseBoard/*")
public class CourseBoardController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CourseBoardService courseBoardService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/{id}")
    @ResponseBody
    public void viewBoardList(@PathVariable Long id) {


    }



}

