package com.hy.demo.domain.community.service;

import com.hy.demo.domain.community.dto.CommunityDto;
import com.hy.demo.domain.community.entity.Community;
import com.hy.demo.domain.community.repository.CommunityRepository;
import com.hy.demo.domain.course.entity.Course;
import com.hy.demo.domain.course.repository.CourseRepository;
import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.repository.UserRepository;
import com.hy.demo.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * service 명명 규칙
 * select -> find
 * modifyCourseEvaluation -> modify
 * insert -> add
 * delete -> delete
 */

@Service

@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {


    private final CommunityRepository communityRepository;


    private final UserRepository userRepository;


    private final CourseRepository courseRepository;


    /**
     * findCommunityList
     * 게시글 검색 및 페이징 ,내가 쓴글 등 조회 통합 service
     */
    @Override
    public Page<CommunityDto> findCommunityList(Long courseId, String search, Pageable pageable, User user) {
        if (!ObjectUtils.isEmpty(user)) {
            return communityRepository.findByCourseIdAndSearch(courseId, pageable, search, user.getUsername());
        } else {
            return communityRepository.findByCourseIdAndSearch(courseId, pageable, search, null);
        }
    }


    /**
     * modifyCommunity
     * 게시글 업데이트 service
     */
    @Override
    @Transactional
    public CommunityDto modifyCommunity(Long communityId, Community updateCommunity, User user) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername())).orElseThrow(() -> new AccessDeniedException("권한없음"));
        Community community = communityRepository.findByIdAndUserId(communityId, findUser.getId()).orElseThrow(() -> new AccessDeniedException("수정 권한없음"));
        community.updateCommunity(updateCommunity);
        Community save = communityRepository.save(community);
        return save.changeDto();
    }


    /**
     * addCommunity
     * 게시글 추가 service
     */
    @Override
    @Transactional
    public CommunityDto addCommunity(Community community, User user, Long courseId) {
        User findUser = userRepository.findByUsername(user.getUsername());
        Course findCourse = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException("없는 강의"));

        Community createCommunity = Community.builder()
                .course(findCourse)
                .contents(community.getContents())
                .title(community.getTitle())
                .user(findUser)
                .build();

        Community save = communityRepository.save(createCommunity);
        CommunityDto communityDto = save.changeDto();
        communityDto.setUser(findUser.changeDto());
        return communityDto;
    }


    /**
     * deleteCommunity
     * 게시글 삭제 service
     */
    @Override
    @Transactional
    public void deleteCommunity(User user, Long communityId) {
        User findUser = userRepository.findByUsername(user.getUsername());
        Long result = communityRepository.deleteByIdAndUserId(communityId, findUser.getId());
        if (result == 0L) {
            throw new AccessDeniedException("삭제권한없음");
        }
    }

    @Override
    public CommunityDto findCommunity(Long communityId) {
        return communityRepository.findDtoById(communityId);
    }


}
