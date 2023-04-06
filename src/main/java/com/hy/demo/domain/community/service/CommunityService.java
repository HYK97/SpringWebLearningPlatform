package com.hy.demo.domain.community.service;

import com.hy.demo.domain.community.dto.CommunityDto;
import com.hy.demo.domain.community.entity.Community;
import com.hy.demo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

public interface CommunityService {
    Page<CommunityDto> findCommunityList(Long courseId, String search, Pageable pageable, User user);

    @Transactional
    CommunityDto modifyCommunity(Long communityId, Community updateCommunity, User user);

    @Transactional
    CommunityDto addCommunity(Community community, User user, Long courseId);

    @Transactional
    void deleteCommunity(User user, Long communityId);

    CommunityDto findCommunity(Long communityId);
}
