package com.hy.demo.Test;

import com.hy.demo.Domain.Board.Entity.Course;
import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import com.hy.demo.Domain.Board.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Board.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PostBean  implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;



    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
      /*  //테스트유저 junit 돌릴때는 주석처리해야댐
        User user = User.builder()
                .username("tuser")
                .role("ROLE_USER")
                .email("user@gmail.com")
                .password(passwordEncoder.encode("user"))
                .build();

        User manager = User.builder()
                .username("tmanager")
                .role("ROLE_MANAGER")
                .email("manager@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();

        Course course = Course.builder()
                .courseName("test")
                .teachName("tmt")
                .courseExplanation("sdasd")
                .user(manager)
                .build();

        CourseEvaluation courseEvaluation1 = CourseEvaluation.builder()
                .comments("Sdsd")
                .scope(4.0)
                .course(course)
                .user(user)
                .build();

        CourseEvaluation courseEvaluation2 = CourseEvaluation.builder()
                .comments("Sdsd")
                .scope(2.0)
                .course(course)
                .user(user)
                .build();
        CourseEvaluation courseEvaluation3 = CourseEvaluation.builder()
                .comments("Sdsd")
                .scope(3.5)
                .course(course)
                .user(user)
                .build();

        userRepository.save(user);
        userRepository.save(manager);
        courseRepository.save(course);

        courseEvaluationRepository.save(courseEvaluation1);
        courseEvaluationRepository.save(courseEvaluation2);
        courseEvaluationRepository.save(courseEvaluation3);*/
    }
}
