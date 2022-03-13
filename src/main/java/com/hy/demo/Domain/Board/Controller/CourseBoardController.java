package com.hy.demo.Domain.Board.Controller;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Service.CourseBoardService;
import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.List;

;

@Controller
@RequestMapping("/courseboard/*")
public class CourseBoardController {

    @Autowired
    private CourseBoardService courseBoardService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping("/info/mycourselist")
    public String myCourseList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size = 9, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        User findUser = userService.findByUsername(principalDetails.getUser());
        Page<CourseDto> myCourseList = courseService.findMyCourseList("", findUser.getId(), pageable);
        model.addAttribute("courseList",myCourseList);
        return "/courseboard/mycourselist";
    }


    @GetMapping("/{id}")
    @PreAuthorize("@authorizationChecker.isAccessBoard(#id)")
    public String viewBoardList(Model model, @PathVariable Long id) {

        List<CourseBoardDto> courseBoardList = courseBoardService.findCourseBoardList(id);
        model.addAttribute("courseList", courseBoardList);

        return "/courseboard/view";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String requestURI = request.getRequestURI().trim();
        String[] split = requestURI.split("/");
        ModelAndView model = new ModelAndView();
        logger.info("requestURI = " + split[2]);
        model.addObject("id", split[2]);
        model.addObject("joinBtn", true);
        model.setViewName("redirect:/course/detailcourse");
        return model;
    }

    @GetMapping("/createCourseBoard")
    @ResponseBody
    public void createCourseBoard(CourseBoard courseBoard) {

        courseBoardService.save(courseBoard);
    }


}

