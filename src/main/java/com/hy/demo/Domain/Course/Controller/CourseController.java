package com.hy.demo.Domain.Course.Controller;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Entity.SummerNoteImage;
import com.hy.demo.Domain.Course.Service.CourseEvaluationService;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.Course.Service.ImageService;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hy.demo.Utils.ObjectUtils.isEmpty;
import static java.lang.Math.floor;

;

@Controller
@RequestMapping("/course/*")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CourseEvaluationService courseEvaluationService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping({"/view"})
    public String course(@RequestParam(defaultValue = "") String search, @PageableDefault(size = 9, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Page<CourseDto> courseDtos = courseService.findCourseList(pageable, search);
        pagingDto(model, courseDtos);
        return "course/view";
    }

    @GetMapping({"/detailcourse"})
    public String detailcourse(String id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Map map = userService.findByUserCourse(id, principalDetails.getUser());
        model.addAttribute("course", map.get("course"));
        model.addAttribute("loginUser", principalDetails.getUsername());
        if (isEmpty(map.get("userCourse"))) {
            model.addAttribute("applicationCheck", null);
        } else {
            boolean b = courseEvaluationService.countByUserAndCourse(principalDetails.getUsername(), id);
            if (b) {//수강평을 썻는지에대해서
                model.addAttribute("commentAccess", 1); //안썻을떄
            } else {
                model.addAttribute("commentAccess", null); //썻을때
            }


            model.addAttribute("applicationCheck", 1);
        }
        return "course/detailcourse";
    }


    @PostMapping({"/application"})
    public String application(String id, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long Lid = Long.parseLong(id);
        try {
            userService.application(Lid, principalDetails.getUser().getUsername());
        } catch (DataIntegrityViolationException e) {
            return "오류처리";
        }
        return "redirect:/courseboard/" + Lid;

    }

    @PostMapping({"/createevaluation"})
    @ResponseBody
    public String createEvaluation(String courseId, String content, String star, String replyId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            courseEvaluationService.addCourseEvaluation(courseId, content, star, principalDetails.getUser(), replyId);
            return "1";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "2";
        } catch (DataIntegrityViolationException e) {
            return "3";
        }
    }

    @GetMapping({"/getCourse/{id}"})
    @ResponseBody
    public CourseDto getCourse(@PathVariable Long id) throws Exception {
        Course findCourse = courseService.findCourseById(id);
        CourseDto courseDto = findCourse.returnDto();
        return courseDto;
    }


    @PostMapping({"/updateevaluation"})
    @ResponseBody
    public String updateEvaluation(String id, String courseId, String content, String star, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        boolean update = courseEvaluationService.modifyCourseEvaluation(id, content, star, principalDetails.getUser(), courseId);
        if (update) {
            return "1";
        } else {
            return "2";
        }

    }


    @PostMapping({"/deleteevaluation/{id}/{courseId}"})
    @ResponseBody
    public String deleteEvaluation(String content, String star, @AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long id, @PathVariable Long courseId) {

        try {
            courseEvaluationService.delete(id, principalDetails.getUser(), courseId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "0";
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "2";
        }
        return "1";
    }


    @GetMapping({"/commentsview"})
    @ResponseBody
    public Map<String, Object> commentsView(@PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal PrincipalDetails principalDetails, String courseId) {
        Long id = Long.parseLong(courseId);
        Page<CourseEvaluationDto> findView = courseEvaluationService.courseEvaluationView(id, pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("CourseEvaluationDto", findView);


        return map;
    }


    @GetMapping({"/createview"})
    public String createView(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return "course/createview";
    }


    @PostMapping({"/create"})
    public String courseCreate(@AuthenticationPrincipal PrincipalDetails principalDetails, MultipartFile thumbnail, String courseName, String teachName, String courseExplanation) {
        Course course = null;
        try {
            SummerNoteImage uploadFile = imageService.store(thumbnail);
            course = Course.builder()
                    .courseExplanation(courseExplanation)
                    .courseName(courseName)
                    .user(principalDetails.getUser())
                    .teachName(teachName)
                    .thumbnail("/image/" + uploadFile.getId())
                    .build();
        } catch (Exception e) {
            //error
            e.printStackTrace();
            return "error/error400";
        }
        courseService.addCourse(course);

        return "redirect:/course/view";

    }

    @PostMapping({"/update/{id}"})
    @ResponseBody
    public String courseUpdate(@PathVariable Long id, @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail, String courseName, String teachName, String courseExplanation) {
        Course course = null;

        try {
            course = courseService.findCourseById(id);
            if (!isEmpty(thumbnail)) {
                try {
                    imageService.deleteImage(course.getThumbnail());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return "2"; //파일 삭제에러
                }
                SummerNoteImage uploadFile = imageService.store(thumbnail);
                course.updateThumbnail("/image/" + uploadFile.getId());
            }
            course.updateCourseExplanation(courseExplanation);
            course.updateCourseName(courseName);
            course.updateTeachName(teachName);
        } catch (Exception e) {
            //error
            e.printStackTrace();
            return "2";
        }
        courseService.addCourse(course);

        return "1";

    }


    private <T> void pagingDto(Model model, Page<T> courseDtos) {
        List<T> content = courseDtos.getContent();
        int pageNumber = courseDtos.getPageable().getPageNumber();
        int totalPages = courseDtos.getTotalPages();
        boolean Previous = courseDtos.hasPrevious();
        boolean Next = courseDtos.hasNext();
        long totalElements = courseDtos.getTotalElements();
        double startPage = floor(pageNumber / 10) * 10 + 1;
        double endPage = startPage + 9 < totalPages ? startPage + 9 : totalPages;
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("course", content);
        model.addAttribute("pageNumber", pageNumber + 1);
        model.addAttribute("Previous", Previous);
        model.addAttribute("Next", Next);
        model.addAttribute("totalElements", totalElements);


    }

    @GetMapping("/info/myCourseList")
    public String myCourseList(Model model, @RequestParam(defaultValue = "") String search, @AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size = 9, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        User findUser = userService.findByUsername(principalDetails.getUser());
        Page<CourseDto> myCourseList = courseService.findMyCourseList(search, findUser.getId(), pageable);

        pagingDto(model, myCourseList);
        return "course/myCreateCourse";
    }


    @GetMapping({"/myCourseView"})
    public String myCourseView(@PageableDefault(size = 9, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails, @RequestParam(defaultValue = "") String search) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        User user = principalDetails.getUser();
        Page<CourseDto> courseDtos;
        try {
            courseDtos = courseService.findMyCourseList(pageable, user, search);
        } catch (AccessDeniedException e) {
            return "error403";
        }
        pagingDto(model, courseDtos);
        return "course/myCourseView";
    }

}

