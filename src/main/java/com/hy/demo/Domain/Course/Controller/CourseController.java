package com.hy.demo.Domain.Course.Controller;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Entity.SummerNoteImage;
import com.hy.demo.Domain.Course.Service.CourseEvaluationService;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.Course.Service.ImageService;
import com.hy.demo.Domain.User.Service.UserService;
import com.hy.demo.Utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping( {"/view"})
        public String course(@PageableDefault(size = 9, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Page<CourseDto> courseDtos=courseService.findCourseList(pageable);
        pagingDto(model, courseDtos);
        return "/course/view";
    }


    @GetMapping( {"/detailcourse"})
    public String detailcourse(String id,Model model,@AuthenticationPrincipal PrincipalDetails principalDetails) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Map map =userService.findByUserCourse(id,principalDetails.getUser());
        model.addAttribute("course",map.get("course"));
        model.addAttribute("loginUser",principalDetails.getUsername());
        if (ObjectUtils.isEmpty(map.get("userCourse"))) {
            model.addAttribute("applicationCheck", null);
        } else {
              boolean b = courseEvaluationService.countByUserAndCourse(principalDetails.getUsername(), id);
            if (b) {//수강평을 썻는지에대해서
                model.addAttribute("commentAccess",1); //안썻을떄
            }else {
                model.addAttribute("commentAccess",null); //썻을때
            }


            model.addAttribute("applicationCheck", 1);
        }
        return "/course/detailcourse";
    }





    @GetMapping( {"/search"})

    public String courseSearch(Model model,String search,@PageableDefault(size = 9, sort = "createDate", direction = Sort.Direction.DESC)Pageable pageable) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Page<CourseDto> courseDtos=courseService.findSearchCourseList(search,pageable);
        pagingDto(model, courseDtos);

        return "/course/view";

    }



    @PostMapping( {"/application"})
    public String courseSearch(String id,@AuthenticationPrincipal PrincipalDetails principalDetails){

        Long Lid= Long.parseLong(id);
        try {
            userService.application(Lid, principalDetails.getUser().getUsername());
        } catch (DataIntegrityViolationException e) {
            return  "오류처리";
        }
        return "redirect:/courseboard/"+Lid;

    }

    @PostMapping( {"/createevaluation"})
    @ResponseBody
    public String createEvaluation(String courseId,String content,String star,String replyId ,@AuthenticationPrincipal PrincipalDetails principalDetails){
        try {
            courseEvaluationService.addCourseEvaluation(courseId, content, star, principalDetails.getUser(),replyId);
            return "1";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "2";
        } catch (DataIntegrityViolationException e) {
            return "3";
        }
    }

    @PostMapping( {"/updateevaluation"})
    @ResponseBody
    public String updateEvaluation(String id,String courseId,String content,String star ,@AuthenticationPrincipal PrincipalDetails principalDetails){

        boolean update = courseEvaluationService.modifyCourseEvaluation(id, content, star, principalDetails.getUser(), courseId);
        if (update) {
            return "1";
        } else {
            return "2";
        }

    }


    @PostMapping( {"/deleteevaluation/{id}/{courseId}"})
    @ResponseBody
    public String deleteEvaluation(String content,String star ,@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable Long id,@PathVariable Long courseId){

        try {
            courseEvaluationService.delete(id, principalDetails.getUser(),courseId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "0";
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "2";
        }
        return "1";
    }






    @GetMapping( {"/commentsview"})
    @ResponseBody
    public Map<String, Object> commentsView(@PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC)Pageable pageable, @AuthenticationPrincipal PrincipalDetails principalDetails, String courseId){
        Long id = Long.parseLong(courseId);
        Page<CourseEvaluationDto> findView = courseEvaluationService.courseEvaluationView(id, pageable);
        Map<String, Object> map =new HashMap<>();
        map.put("CourseEvaluationDto",findView);


        return map;
    }




    @GetMapping( {"/createview"})
    public String createView(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return "/course/createview";
    }


    @PostMapping( {"/create"})
    public String testCreate(@AuthenticationPrincipal PrincipalDetails principalDetails,MultipartFile thumbnail,String courseName,String teachName,String courseExplanation) {
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
            return "/error/error400";
        }
        courseService.addCourse(course);

        return "redirect:/course/view";

    }



    private <T> void pagingDto(Model model, Page<T> courseDtos) {
        List<T> content = courseDtos.getContent();
        int pageNumber = courseDtos.getPageable().getPageNumber();
        int totalPages = courseDtos.getTotalPages();
        boolean Previous = courseDtos.hasPrevious();
        boolean Next = courseDtos.hasNext();
        long totalElements = courseDtos.getTotalElements();

        model.addAttribute("course", content);
        model.addAttribute("pageNumber", pageNumber + 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("Previous", Previous);
        model.addAttribute("Next", Next);
        model.addAttribute("totalElements", totalElements);

        logger.info("1현재페이지 = " + pageNumber);
        logger.info("콘텐츠갯수 = " + totalElements);
        logger.info("전체페이지 = " + totalPages);
        logger.info("이전페이지있냐 = " + Previous);
        logger.info("다음페이지있냐 = " + Next);
    }



}

