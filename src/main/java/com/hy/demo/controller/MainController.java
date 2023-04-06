package com.hy.demo.controller;

import com.hy.demo.domain.course.dto.CourseDto;
import com.hy.demo.domain.course.service.CourseEvaluationService;
import com.hy.demo.domain.course.service.CourseService;
import com.hy.demo.domain.user.dto.UserDto;
import com.hy.demo.domain.user.service.UserService;
import com.hy.demo.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/main/*")
@RequiredArgsConstructor
public class MainController {


    private final CourseService courseService;

    private final UserService userService;

    private final CourseEvaluationService courseEvaluationService;


    @GetMapping({"/index"})
    public String index(Model model) {

        final List<UserDto> userList = userService.rankRandomUser(3);
        final List<CourseDto> headerView = courseService.randomCourseList(5);
        final List<CourseDto> firstCourse = courseService.findRankingScopeAvgCourse(1);
        final List<CourseDto> secondCourse = courseService.findRankingEvaluationCountCourse(1);
        final List<CourseDto> thirdCourse = courseService.findRankingUserCourseCountCourse(1);// 쿼리 분리
        if (!ObjectUtils.isEmpty(thirdCourse)) {
            final Double avgScopeByCourseId = courseEvaluationService.findAvgScopeByCourseId(thirdCourse.get(0).getId());
            thirdCourse.get(0).updateScope(avgScopeByCourseId);
            model.addAttribute("thirdCourse", thirdCourse);
        }
        model.addAttribute("headerView", headerView);
        model.addAttribute("headerViewSize", new ArrayList<>(Arrays.asList(new String[]{"0", "1", "2", "3", "4"})));
        model.addAttribute("userList", userList);
        model.addAttribute("firstCourse", firstCourse);
        model.addAttribute("secondCourse", secondCourse);

        return "main/index";
    }

}
