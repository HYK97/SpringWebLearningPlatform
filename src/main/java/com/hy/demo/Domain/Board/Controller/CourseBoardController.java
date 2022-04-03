package com.hy.demo.Domain.Board.Controller;


import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Service.CourseBoardService;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Service.FileService;
import com.hy.demo.Domain.User.Service.UserService;
import com.hy.demo.Utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
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

    @Autowired
    private FileService fileService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping("/{id}")
    @PreAuthorize("@authorizationChecker.isAccessBoard(#id)")
    public String viewBoardList(Model model, @PathVariable Long id) {
        model.addAttribute("courseId", id);
        return "courseboard/view";
    }

    @PostMapping("/viewPlus/{id}")
    @ResponseBody
    public void viewPlus(Model model, @PathVariable Long id) {
        courseBoardService.viewPlus(id);
    }


    @GetMapping("/data/{id}")
    @ResponseBody
    public List<CourseBoardDto> Data(@PathVariable Long id) {
        List<CourseBoardDto> courseBoardList = courseBoardService.findCourseBoardList(id);

        return courseBoardList;
    }


    @GetMapping("/BoardManagement/{id}")
    @PreAuthorize("@authorizationChecker.isManagementBoard(#id)")
    public String BoardManagement(Model model, @PathVariable Long id) {
        model.addAttribute("courseId", id);
        return "courseboard/management";
    }


    @PostMapping("/deleteCourseBoard/{id}")
    @ResponseBody
    public String deleteBoard(@PathVariable Long id) {
        logger.info("id = " + id);
        try {
            courseBoardService.deleteBoardAndFiles(id);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            return "2"; //실패
        }

        return "1"; //성공
    }


    @PostMapping("/updateCourseBoard/{id}")
    @ResponseBody
    public String updateCourseBoard(@PathVariable Long id, String title, String contents, @RequestParam(value = "file", required = false) List<MultipartFile> file) throws IOException {
        //파일 확인
        try {
            courseBoardService.updateCourseBoard(id, file, title, contents);
        } catch (Exception e) {
            e.printStackTrace();
            return "2"; //실패
            //실패
        }
        return "1"; //성공
    }


    @GetMapping({"/getCourseBoard/{id}"})
    @ResponseBody
    public CourseBoardDto getCourseBoard(@PathVariable Long id) throws Exception {
        CourseBoardDto findCourseDto = courseBoardService.findCourseBoardByCourseBoardId(id);
        return findCourseDto;
    }


    @PostMapping("/createBoard/{id}")
    @ResponseBody
    public String createBoard(@PathVariable Long id, String title, String contents, @RequestParam(value = "file", required = false) List<MultipartFile> file) {
        List<FileDto> fileDtos = new ArrayList<>();
        Course findCourse;
        try {
            findCourse = courseService.findCourseById(id);
        } catch (Exception e) {
            return "3"; //잘못된 courseId
        }
        CourseBoard courseBoard = CourseBoard.builder()
                .title(title)
                .contents(contents)
                .views(0L)
                .course(findCourse)
                .build();
        if (!ObjectUtils.isEmpty(file)) {
            try {
                fileDtos = fileService.localSaveFile(file);
                logger.info("fileDtos = " + fileDtos.size());
            } catch (IOException e) {
                e.printStackTrace();
                return "2"; //파일오류
            }
        }
        courseBoardService.save(courseBoard, fileDtos);
        return "1";//성공
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (e.getReason().equals("1")) {
            String requestURI = request.getRequestURI().trim();
            String[] split = requestURI.split("/");
            ModelAndView model = new ModelAndView();
            logger.info("requestURI = " + split[2]);
            model.addObject("id", split[2]);
            model.addObject("joinBtn", true);
            model.setViewName("redirect:/course/detailcourse");
            return model;
        } else if (e.getReason().equals("2")) {
            throw new AccessDeniedException("403 에러");
        } else {
            throw new AccessDeniedException("403 에러");
        }
    }


}

