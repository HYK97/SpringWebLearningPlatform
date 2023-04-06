package com.hy.demo.domain.community.repository;

import com.hy.demo.domain.community.dto.CommunityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityRepositoryCustom {

    Page<CommunityDto> findByCourseIdAndSearch(Long courseId, Pageable pageable, String search, String username);

    CommunityDto findDtoById(Long courseId);
}
