package com.hy.demo.Domain.Board.Controller;


import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Service.CourseBoardService;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.Course.Service.ImageService;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

;import java.util.List;

@Controller
@RequestMapping("/courseBoard/*")
public class CourseBoardController {

    @Autowired
    private CourseBoardService courseBoardService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @GetMapping("/{id}")
    public String viewBoardList(Model model, @PathVariable Long id) {
        List<CourseBoardDto> courseBoardList = courseBoardService.findCourseBoardList(id);
        model.addAttribute("courseList",courseBoardList);
        return "/courseboard/view";
    }
    @GetMapping("/createCourseBoard")
    @ResponseBody
    public void createCourseBoard(CourseBoard courseBoard) {

        courseBoardService.save(courseBoard);
    }



}

