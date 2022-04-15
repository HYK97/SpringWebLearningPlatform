package com.hy.demo.Domain.Community.Repository;

import com.hy.demo.Domain.Community.Dto.CommunityDto;
import com.hy.demo.Domain.User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityRepositoryCustom {

    Page<CommunityDto> findByCourseIdAndSearch(Long courseId, Pageable pageable, String search, String username);
    CommunityDto findDtoById(Long courseId);
}
