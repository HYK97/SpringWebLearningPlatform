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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/course/*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping( {"/view"})
    @ResponseBody
    public List<CourseDto> course(Pageable pageable) {
        System.out.println("courseService.viewCourse(pageable) = " + courseService.viewCourse(pageable).toString());
        return courseService.viewCourse(pageable);

    }

    @GetMapping( {"/search"})
    @ResponseBody
    public List<CourseDto> courseSearch(String search,Pageable pageable) {
        System.out.println("search = " + courseService.searchCourse(search,pageable));
        return courseService.searchCourse(search,pageable);
    }



    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @GetMapping( {"/create"})
    public String testCreate(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        System.out.println("principalDetails.getUser().\\ = " + principalDetails.getUser().toString());
        User user = userRepository.findByUsername(principalDetails.getUser().getUsername());
        Course course = Course.builder()
                .courseName("test1")
                .user(user)
                .heart(0)
                .build();

        courseService.createCourse(course);
        return "/course/view";
    }
}
