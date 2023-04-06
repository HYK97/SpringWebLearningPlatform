package com.hy.demo.domain.community.service;

import com.hy.demo.domain.community.dto.CommunityDto;
import com.hy.demo.domain.community.entity.Community;
import com.hy.demo.domain.community.repository.CommunityRepository;
import com.hy.demo.domain.course.entity.Course;
import com.hy.demo.domain.course.repository.CourseRepository;
import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommunityServiceTest {


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private CommunityService communityService;
    private Long courseId1;

    private Long courseId2;

    private Long userId;
    private Long communityId1;


    @BeforeEach
    public void setup() {

        User manager1 = User.builder()
                .username("manager1")
                .role("ROLE_MANAGER")
                .email("manager1@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();
        User manager2 = User.builder()
                .username("manager2")
                .role("ROLE_MANAGER")
                .email("manager2@gmail.com")
                .password(passwordEncoder.encode("manager"))
                .build();

        Course course1 = Course.builder()
                .courseName("test1")
                .user(manager1)
                .build();

        Course course2 = Course.builder()
                .courseName("test1")
                .user(manager2)
                .build();

        Community community1 = Community.builder()
                .course(course1)
                .contents("test1")
                .user(manager1)
                .title("title1")
                .build();

        Community community2 = Community.builder()
                .course(course1)
                .contents("test2")
                .user(manager1)
                .title("title2")
                .build();

        Community community3 = Community.builder()
                .course(course1)
                .contents("re1")
                .user(manager2)
                .title("re1")
                .build();


        Community community4 = Community.builder()
                .course(course2)
                .contents("re2")
                .user(manager2)
                .title("re2")
                .build();


        Community community5 = Community.builder()
                .course(course2)
                .contents("re3")
                .user(manager1)
                .title("re3")
                .build();
        userId = userRepository.save(manager1).getId();
        userRepository.save(manager2);
        courseId1 = courseRepository.save(course1).getId();
        courseId2 = courseRepository.save(course2).getId();
        communityId1 = communityRepository.save(community1).getId();
        communityRepository.save(community2);
        communityRepository.save(community3);
        communityRepository.save(community4);
        communityRepository.save(community5);

    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
        communityRepository.deleteAll();
    }


    /**
     * 첫번째 manager1이 course2에서 썻던 자기자신글 중 re로 검색 한 모든 게시글조회
     * 두번째 course2의 모든 게시글 조회
     */
    @Test
    public void findCommunityList() {
        //given
        PageRequest page = PageRequest.of(0, 10);
        String search = "3";
        User user = userRepository.findById(userId).get();
        //when
        List<CommunityDto> content1 = communityService.findCommunityList(courseId2, search, page, user).getContent();
        List<CommunityDto> content2 = communityService.findCommunityList(courseId2, null, page, null).getContent();
        //then
        assertThat(content1.size()).isEqualTo(1);
        assertThat(content2.size()).isEqualTo(2);
        assertThat(content1).extracting("title", "user.username", "contents")
                .containsOnly(
                        tuple("re3", "manager1", "re3")
                );
        assertThat(content2).extracting("title", "user.username", "contents")
                .containsOnly(
                        tuple("re2", "manager2", "re2"),
                        tuple("re3", "manager1", "re3")
                );
    }


    @Test
    public void modifyCommunity() throws Exception {
        //given
        Community community = communityRepository.findById(communityId1).get();
        User user = userRepository.findById(userId).get();
        Community update = Community.builder()
                .contents("업데이트 콘텐츠")
                .title("업데이트 타이틀")
                .build();
        community.updateCommunity(update);
        //when
        communityService.modifyCommunity(community.getId(), update, user);
        //then
        Community updateCommunity = communityRepository.findById(communityId1).get();
        assertThat(updateCommunity).extracting("title", "user.username", "contents")
                .containsOnly(
                        "업데이트 타이틀", "manager1", "업데이트 콘텐츠"
                );

    }


    @Test
    public void failModifyCommunity() throws Exception {
        //given
        Community community = communityRepository.findById(communityId1).get();
        User user1 = userRepository.findById(userId).get();
        User user2 = User.builder().id(999999L).build();
        Community update = Community.builder()
                .contents("업데이트 콘텐츠")
                .title("업데이트 타이틀")
                .build();
        community.updateCommunity(update);
        //when then

        assertThrows(AccessDeniedException.class, () -> {
            communityService.modifyCommunity(community.getId(), update, user2);
        });
        assertThrows(AccessDeniedException.class, () -> {
            communityService.modifyCommunity(999999L, update, user1);
        });


    }

    @Test
    public void addCommunity() throws Exception {
        //given
        Community createCommunity = Community.builder()
                .title("새로만듦")
                .contents("새로만듦")
                .build();
        User user = userRepository.findById(userId).get();
        //when
        CommunityDto communityDto = communityService.addCommunity(createCommunity, user, courseId1);
        //then

        Community updateCommunity = communityRepository.findById(communityDto.getId()).get();
        assertThat(updateCommunity).extracting("title", "user.username", "contents")
                .containsOnly(
                        "새로만듦", "manager1", "새로만듦"
                );

    }


    @Test
    public void failAddCommunity() throws Exception {
        //given
        Community createCommunity = Community.builder()
                .title("새로만듦")
                .contents("새로만듦")
                .build();
        User user = userRepository.findById(userId).get();
        //when then
        assertThrows(EntityNotFoundException.class, () -> {
            communityService.addCommunity(createCommunity, user, 99999L);
        });
    }


    @Test
    public void failDeleteCommunity() throws Exception {
        //given
        User user = userRepository.findById(userId).get();
        //when then
        assertThrows(AccessDeniedException.class, () -> {
            communityService.deleteCommunity(user, 9999L);
        });
    }

    @Test
    public void deleteCommunity() throws Exception {
        //given
        User user = userRepository.findById(userId).get();

        //when
        communityService.deleteCommunity(user, communityId1);
        //then
        assertThrows(NoSuchElementException.class, () -> {
            communityRepository.findById(communityId1).get();
        });
    }


}