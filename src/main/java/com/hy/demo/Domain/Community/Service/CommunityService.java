package com.hy.demo.Domain.Community.Service;

import com.hy.demo.Domain.Community.Dto.CommunityDto;
import com.hy.demo.Domain.Community.Entity.Community;
import com.hy.demo.Domain.User.Entity.User;
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
