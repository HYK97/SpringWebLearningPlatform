package com.hy.demo.domain.community.repository;

import com.hy.demo.domain.community.dto.CommunityDto;
import com.hy.demo.domain.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long>, CommunityRepositoryCustom {

    Page<CommunityDto> findByCourseIdAndSearch(Long courseId, Pageable pageable, String search, String username);

    Optional<Community> findByIdAndUserId(Long id, Long userId);

    Long deleteByIdAndUserId(Long id, Long userId);

    CommunityDto findDtoById(Long courseId);
}
