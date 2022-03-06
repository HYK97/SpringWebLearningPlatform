package com.hy.demo.Domain.Board.Controller;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Dto.CourseEvaluationDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.SummerNoteImage;
import com.hy.demo.Domain.Board.Service.CourseEvaluationService;
import com.hy.demo.Domain.Board.Service.CourseService;
import com.hy.demo.Domain.Board.Service.ImageService;
import com.hy.demo.Domain.User.Service.UserService;
import com.hy.demo.Utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
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

        Page<CourseDto> courseDtos=courseService.viewCourse(pageable);

        pagingDto(model, courseDtos);
        return "/course/view";
    }

    @GetMapping( {"/detailcourse"})
    public String detailcourse(String id,Model model,@AuthenticationPrincipal PrincipalDetails principalDetails) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Map map =userService.findByUserCourse(id,principalDetails.getUser());
        model.addAttribute("course",map.get("course"));

        if (ObjectUtils.isEmpty(map.get("userCourse"))) {
            model.addAttribute("applicationCheck", null);
        } else {
            model.addAttribute("applicationCheck", 1);
        }
        return "/course/detailcourse";
    }





    @GetMapping( {"/search"})

    public String courseSearch(Model model,String search,@PageableDefault(size = 9, sort = "createDate", direction = Sort.Direction.DESC)Pageable pageable) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Page<CourseDto> courseDtos=courseService.searchCourse(search,pageable);
        pagingDto(model, courseDtos);

        return "/course/view";

    }



    @PostMapping( {"/application"})
    public String courseSearch(String id,@AuthenticationPrincipal PrincipalDetails principalDetails){

        try {
            Long Lid= Long.parseLong(id);
            userService.application(Lid, principalDetails.getUser().getUsername());
        } catch (DataIntegrityViolationException e) {
            return  "오류처리";
        }
        return "redirect:/course/view";

    }






    @PostMapping( {"/commentsview"})
    @ResponseBody
    public List<CourseEvaluationDto> commentsView(Model model, @PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC)Pageable pageable, @AuthenticationPrincipal PrincipalDetails principalDetails, String courseId){
        Long id = Long.parseLong(courseId);
        Page<CourseEvaluationDto> findView = courseEvaluationService.courseEvaluationView(id, pageable);
        model.addAttribute("CourseEvaluationDto",findView);

        List<CourseEvaluationDto> content = findView.getContent();
        return content;
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
        courseService.createCourse(course);

        return "redirect:/course/view";

    }



    private void pagingDto(Model model, Page<CourseDto> courseDtos) {
        List<CourseDto> content = courseDtos.getContent();
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

