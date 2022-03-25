package com.hy.demo.Test;

import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Repository.CourseBoardRepository;
import com.hy.demo.Domain.Board.Service.CourseBoardService;
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

import java.util.ArrayList;
import java.util.List;

@Component
public class PostBean implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;

    @Autowired
    private CourseBoardRepository courseBoardRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    List<Course> courseList= new ArrayList<>();
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //테스트유저 junit 돌릴때는 주석처리해야댐
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .username("tuser" + i)
                    .role("ROLE_USER")
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("user"))
                    .build();

            User manager = User.builder()
                    .username("tmanager" + i)
                    .role("ROLE_MANAGER")
                    .email("manager@gmail.com")
                    .password(passwordEncoder.encode("manager"))
                    .build();
            userRepository.save(user);
            userRepository.save(manager);

            for (int j = 0; j < 20; j++) {

                Course course = Course.builder()
                        .courseName("test" + j)
                        .teachName(user.getUsername())
                        .courseExplanation("sdasd")
                        .user(manager)
                        .build();
                courseRepository.save(course);
                courseList.add(course);
            }

            for (Course course : courseList) {

                for (int f = 0; f < 5; f++) {
                    CourseBoard courseBoard = CourseBoard.builder()
                            .course(course)
                            .contents("courseBoardContents" + f)
                            .views(3L)
                            .title("courseBoardTitle" + f)
                            .build();
                    courseBoardRepository.save(courseBoard);
                }


                CourseEvaluation courseEvaluations = CourseEvaluation.builder()
                        .comments("good" + i)
                        .scope((double) (i / 3))
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

            }



        }


    }
}
