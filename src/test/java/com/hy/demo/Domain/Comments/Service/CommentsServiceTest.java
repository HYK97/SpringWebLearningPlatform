package com.hy.demo.Domain.Comments.Service;

import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Repository.CourseBoardRepository;
import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.Comments.Entity.Comments;
import com.hy.demo.Domain.Comments.Repository.CommentsRepository;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.File.Repository.FileRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommentsServiceTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private CourseBoardRepository courseBoardRepository;
    @Autowired
    private CommentsService commentsService;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileRepository fileRepository;


    private List<Long> userIdList = new ArrayList<Long>();
    private List<Long> courseIdList = new ArrayList<Long>();
    private List<Long> courseEvaluationIdList = new ArrayList<Long>();
    private List<Long> courseBoardIdList = new ArrayList<Long>();
    private List<Long> commentsIdList = new ArrayList<Long>();

    @BeforeEach
    public void setup() {
        for (int i = 0; i < 5; i++) { // 유저 및 코스
            User user;
            Course course;
            Long userId;
            if (i % 2 == 0) {
                user = User.builder()
                        .username("user" + i)
                        .role("ROLE_USER")
                        .email("user" + i + "@gmail.com")
                        .password(passwordEncoder.encode("user" + i))
                        .build();
                userId = userRepository.save(user).getId();
                userIdList.add(userId);
            } else {
                user = User.builder()
                        .username("manager" + i)
                        .role("ROLE_MANAGER")
                        .email("manager" + i + "@gmail.com")
                        .password(passwordEncoder.encode("manager" + i))
                        .build();

                course = Course.builder()
                        .courseName("courseName" + user.getUsername())
                        .user(user)
                        .courseExplanation("코스 " + i + "번 입니다.")
                        .build();
                userId = userRepository.save(user).getId();
                userIdList.add(userId);
                Long courseId = courseRepository.save(course).getId();
                courseIdList.add(courseId);

                for (int j = 0; j < 2; j++) {
                    CourseBoard courseBoard = CourseBoard.builder()
                            .course(course)
                            .title("courseName" + i + "courseBoardTitle" + j)
                            .contents("courseBoardContents" + j)
                            .views(3L).build();
                    CourseBoard courseBoardE = courseBoardRepository.save(courseBoard);
                    courseBoardIdList.add(courseBoardE.getId());

                    for (int l = 0; l < 7; l++) {
                        Comments comments = Comments.builder()
                                .user(user)
                                .courseBoard(courseBoardE)
                                .comments("testComment" + l)
                                .build();
                        Long id = commentsRepository.save(comments).getId();
                        commentsIdList.add(id);
                        for (int k = 0; k < 7; k++) {
                            Comments reply = Comments.builder()
                                    .user(user)
                                    .parent(comments)
                                    .courseBoard(courseBoardE)
                                    .comments("testReply" + l)
                                    .build();
                            commentsRepository.save(reply);
                        }
                    }
                }
            }
        }


    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
        courseBoardRepository.deleteAll();
        commentsRepository.deleteAll();
    }


    @Test
    public void findCommentsListByCourseId() throws Exception {
        //given
        PageRequest page = PageRequest.of(0, 3);
        //when
        Page<CommentsDto> commentsListByCourseId = commentsService.findCommentsListByCourseId(courseBoardIdList.get(0), page, 1);
        List<CommentsDto> content = commentsListByCourseId.getContent();
        //then


        assertThat(content).extracting("id", "user.username", "comments", "replyCounts")
                .containsOnly(
                        tuple(1L, "manager1", "testComment0", 7L),
                        tuple(9L, "manager1", "testComment1", 7L),
                        tuple(17L, "manager1", "testComment2", 7L)
                );


    }
}