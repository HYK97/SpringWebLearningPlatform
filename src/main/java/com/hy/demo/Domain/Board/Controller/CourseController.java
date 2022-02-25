package com.hy.demo.Domain.Board.Controller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Service.CourseService;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Controller
@RequestMapping("/course/*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping( {"/view"})
        public String course(@PageableDefault(size = 9, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Page<CourseDto> courseDtos=courseService.viewCourse(pageable);
        pagingDto(model, courseDtos);
        return "/course/view";
    }




    @GetMapping( {"/search"})

    public String courseSearch(Model model,String search,@PageableDefault(size = 9, sort = "createDate", direction = Sort.Direction.DESC)Pageable pageable) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Page<CourseDto> courseDtos=courseService.searchCourse(search,pageable);
        pagingDto(model, courseDtos);

        return "/course/view";

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





    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping( {"/create"})
    public String testCreate(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        System.out.println("principalDetails.getUser().\\ = " + principalDetails.getUser().toString());
        User user = userRepository.findByUsername(principalDetails.getUser().getUsername());
        Course course = Course.builder()
                .courseName("test1")
                .user(user)
                .heart(0)
                .build();
        Course course2 = Course.builder()
                .courseName("asd")
                .user(user)
                .heart(0)
                .build();

        courseService.createCourse(course);
        courseService.createCourse(course2);
        return "/course/view";
    }
}
