package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import com.hy.demo.Domain.Course.Repository.CourseEvaluationRepository;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Entity.File;
import com.hy.demo.Domain.File.Repository.FileRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseBoardRepositoryImplTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseEvaluationRepository courseEvaluationRepository;

    @Autowired
    private CourseBoardRepository courseBoardRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    Logger logger;

    private List<Long> userIdList = new ArrayList<Long>();
    private List<Long> courseIdList = new ArrayList<Long>();
    private List<Long> courseEvaluationIdList = new ArrayList<Long>();
    private List<Long> replyIdList = new ArrayList<Long>();
    private List<Long> courseBoardIdList = new ArrayList<Long>();

    @BeforeEach
    public void setup() {
        for (int i = 0; i < 20; i++) { // 유저 및 코스
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

                for (int j = 0; j < 5; j++) {
                CourseBoard courseBoard = CourseBoard.builder()
                        .course(course)
                        .title("courseName"+i+"courseBoardTitle"+j)
                        .contents("courseBoardContents"+j)
                        .views(3L).build();
                    CourseBoard courseBoardE = courseBoardRepository.save(courseBoard);
                    courseBoardIdList.add(courseBoardE.getId());

                    File file =File.builder()
                            .filePath("ss")
                            .fileSize(2L)
                            .origFileName("testFile")
                            .courseBoard(courseBoardE)
                            .build();
                    fileRepository.save(file);
                }

            }
        }

        List<User> allUser = userRepository.findByRole("ROLE_USER"); //댓글
        List<Course> allCourse = courseRepository.findAll();
        for (Course course : allCourse) {
            for (int i = 0; i < 3; i++) {
                CourseEvaluation courseEvaluation = CourseEvaluation.builder()
                        .user(allUser.get(i))
                        .scope((double) (i + 8 / 3))
                        .comments("courseEvaluation" + i)
                        .course(course)
                        .build();
                Long courseEvaluationId = courseEvaluationRepository.save(courseEvaluation).getId();
                courseEvaluationIdList.add(courseEvaluationId);
                //reply
                String teachName = allUser.get(i).getUsername();
                User findManger = userRepository.findByUsername(teachName);
                CourseEvaluation reply = CourseEvaluation.builder()
                        .user(findManger)
                        .comments("reply" + i)
                        .course(course)
                        .replyId(courseEvaluationId)
                        .build();
                Long replyId = courseEvaluationRepository.save(reply).getId();
                replyIdList.add(replyId);
            }


        }


    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
        courseEvaluationRepository.deleteAll();
        courseBoardRepository.deleteAll();
    }


    @Test
    public void findByCourseIdNotContents() throws Exception{
    //given
        Long courseId =courseIdList.get(0);
    //when
        List<CourseBoardDto> findCourseBoard = courseBoardRepository.findByCourseIdNotContents(courseId);
        //then
        assertThat(findCourseBoard.size()).isEqualTo(5);
        assertThat(findCourseBoard).extracting("title","contents","views")
                .containsOnly(
                        tuple("courseName1courseBoardTitle0","courseBoardContents0",3L),
                        tuple("courseName1courseBoardTitle1","courseBoardContents1",3L),
                        tuple("courseName1courseBoardTitle2","courseBoardContents2",3L),
                        tuple("courseName1courseBoardTitle3","courseBoardContents3",3L),
                        tuple("courseName1courseBoardTitle4","courseBoardContents4",3L)
                );

    }


    @Test
    public void findByCourseBoardId() throws Exception{
    //given

    //when
        CourseBoard findDto = courseBoardRepository.findByCourseBoardId(courseBoardIdList.get(0));
        CourseBoardDto courseBoardDto = findDto.changeDto();
        //then
        List<FileDto> files = courseBoardDto.getFiles();
        for (FileDto file : files) {
            logger.info("file.getOrigFileName() = " + file.getOrigFileName());
        }
        
    }
}