package com.hy.demo.Test;

import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Repository.CourseBoardRepository;
import com.hy.demo.Domain.Community.Entity.Community;
import com.hy.demo.Domain.Community.Repository.CommunityRepository;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import com.hy.demo.Domain.Course.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Entity.UserCourse;
import com.hy.demo.Domain.User.Repository.UserCourseRepository;
import com.hy.demo.Domain.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostData implements ApplicationListener<ContextRefreshedEvent> {


    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;
    private final CourseEvaluationRepository courseEvaluationRepository;
    private final CommunityRepository communityRepository;
    private final CourseBoardRepository courseBoardRepository;
    private final PasswordEncoder passwordEncoder;

    List<Course> courseList = new ArrayList<>();
    List<CourseBoard> courseBoard = new ArrayList<>();
    List<User> users = new ArrayList<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //테스트유저 junit 돌릴때는 주석처리해야댐

        for (int i = 0; i < 20; i++) {
            User user = User.builder()
                    .username("tuser" + i)
                    .role("ROLE_USER")
                    .email("user@gmail.com")
                    .selfIntroduction("hi" + i)
                    .nickname("nick" + i)
                    .password(passwordEncoder.encode("user"))
                    .build();

            User manager = User.builder()
                    .username("tmanager" + i)
                    .role("ROLE_MANAGER")
                    .email("manager@gmail.com")
                    .nickname("mnick" + i)
                    .selfIntroduction("hi" + i)
                    .password(passwordEncoder.encode("manager"))
                    .build();
            userRepository.save(user);
            userRepository.save(manager);
            users.add(user);
            users.add(manager);


        }
        int index = 0;
        for (User user : users) {
            Course course = Course.builder()
                    .courseName("test" + index)
                    .courseExplanation("sdasd")
                    .user(user)
                    .build();
            courseRepository.save(course);
            courseList.add(course);
            index++;
        }


        for (Course course : courseList) {
            for (int f = 0; f < 2; f++) {
                CourseBoard courseBoard = CourseBoard.builder()
                        .course(course)
                        .contents("courseBoardContents" + f)
                        .views(3L)
                        .title("courseBoardTitle" + f)
                        .build();
                courseBoardRepository.save(courseBoard);
            }
            for (int i = 0; i < 125; i++) {
                Community build = Community.builder().title("title" + i)
                        .contents("content" + i)
                        .user(users.get(i % 15))
                        .course(course).build();
                communityRepository.save(build);
            }
        }

        for (Course course : courseList) {
            int jndex = 0;
            for (User user : users) {
                UserCourse userCourse = UserCourse.builder()
                        .course(course)
                        .user(user)
                        .build();
                UserCourse save = userCourseRepository.save(userCourse);
                userCourse.setCreateDate(Timestamp.valueOf(LocalDateTime.now().plusDays(jndex)));
                userCourseRepository.save(userCourse);
                jndex++;
            }
        }


        for (Course course : courseList) {
            int jndex = 0;
            for (User user : users) {
                CourseEvaluation courseEvaluations = CourseEvaluation.builder()
                        .comments("good" + jndex)
                        .scope((double) (jndex % 6))
                        .course(course)
                        .user(user)
                        .build();
                CourseEvaluation save1 = courseEvaluationRepository.save(courseEvaluations);
                courseEvaluations.setCreateDate(Timestamp.valueOf(LocalDateTime.now().minusDays(jndex)));
                courseEvaluationRepository.save(courseEvaluations);

//
//                CourseEvaluation reply = CourseEvaluation.builder()
//                        .comments("답글 리플")
//                        .replyId(id)
//                        .course(course)
//                        .user(manager)
//                        .build();
//                courseEvaluationRepository.save(reply);

                jndex++;
            }


        }


/////////////////////////////////////////////////////////////////////////
//      User manager = User.builder()
//                    .username("tmanager")
//                    .role("ROLE_MANAGER")
//                    .email("manager@gmail.com")
//                    .password(passwordEncoder.encode("manager"))
//                    .build();
//                userRepository.save(manager);
//        for (int i = 0; i < 125; i++) {
//
//
//
//
//                Course course = Course.builder()
//                        .courseName("test" + i)
//                        .teachName(manager.getUsername())
//                        .courseExplanation("sdasd")
//                        .user(manager)
//                        .build();
//                courseRepository.save(course);
//                courseList.add(course);
//
//
//
//
//                CourseBoard courseBoards = CourseBoard.builder()
//                            .course(course)
//                            .contents("courseBoardContents" + i)
//                            .views(3L)
//                            .title("courseBoardTitle" + i)
//                            .build();
//                CourseBoard save = courseBoardRepository.save(courseBoards);
//                courseBoard.add(save);
//
//
//
//            }
//        for (Course course : courseList) {
//            for (int i = 0; i < 125; i++) {
//
//                CourseEvaluation courseEvaluations = CourseEvaluation.builder()
//                        .comments("good" + i)
//                        .scope((double) (i / 3))
//                        .course(course)
//                        .user(manager)
//                        .build();
//                Long id = courseEvaluationRepository.save(courseEvaluations).getId();
//            }
//        }
//
//        for (CourseBoard course : courseBoard) {
//            for (int i = 0; i < 125; i++) {
//                Comments comments = Comments.builder()
//                        .user(manager)
//                        .courseBoard(course)
//                        .comments("comments"+i)
//                        .build();
//
//                commentsRepository.save(comments);
//            }
//
//
//        }


    }

}
