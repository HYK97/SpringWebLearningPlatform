package com.hy.demo.Controller;

import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Service.CourseEvaluationService;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.User.Dto.UserDto;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller

@RequestMapping("/main/*")
public class MainController {


    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;
    @Autowired
    private CourseEvaluationService courseEvaluationService;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping({"/index"})
    public String index(Model model) {

        List<UserDto> userList = userService.rankRandomUser(3);
        List<CourseDto> headerView = courseService.randomCourseList(5);
        List<CourseDto> firstCourse = courseService.findRankingScopeAvgCourse(1);
        List<CourseDto> secondCourse = courseService.findRankingEvaluationCountCourse(1);
        List<CourseDto> thirdCourse = courseService.findRankingUserCourseCountCourse(1);// 쿼리 분리
        Double avgScopeByCourseId = courseEvaluationService.findAvgScopeByCourseId(thirdCourse.get(0).getId());
        thirdCourse.get(0).updateScope(avgScopeByCourseId);
        model.addAttribute("headerView", headerView);
        model.addAttribute("headerViewSize", new ArrayList<>(Arrays.asList(new String[]{"0", "1", "2", "3", "4"})));
        model.addAttribute("userList", userList);
        model.addAttribute("firstCourse", firstCourse);
        model.addAttribute("secondCourse", secondCourse);
        model.addAttribute("thirdCourse", thirdCourse);

        return "main/index";
    }

}
