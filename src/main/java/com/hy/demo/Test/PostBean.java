package com.hy.demo.Test;

import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import com.hy.demo.Domain.Course.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
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
        //테스트유저 junit 돌릴때는 주석처리해야댐
      /*  for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .username("tuser"+i)
                    .role("ROLE_USER")
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("user"))
                    .build();

            User manager = User.builder()
                    .username("tmanager"+i)
                    .role("ROLE_MANAGER")
                    .email("manager@gmail.com")
                    .password(passwordEncoder.encode("manager"))
                    .build();
            userRepository.save(user);
            userRepository.save(manager);

            Course course = Course.builder()
                    .courseName("test"+i)
                    .teachName(user.getUsername())
                    .courseExplanation("sdasd")
                    .user(manager)
                    .build();
            courseRepository.save(course);


                CourseEvaluation courseEvaluations = CourseEvaluation.builder()
                        .comments("good"+i)
                        .scope((double) (i/3))
                        .course(course)
                        .user(user)
                        .build();
            Long id = courseEvaluationRepository.save(courseEvaluations).getId();


            CourseEvaluation reply = CourseEvaluation.builder()
                    .comments("답글 리플")
                    .replyId(id)
                    .course(course)
                    .user(manager)
                    .build();
            courseEvaluationRepository.save(reply);
        }*/




    }
}
