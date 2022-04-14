package com.hy.demo.Domain.Community.Repository;

import com.hy.demo.Domain.Community.Dto.CommunityDto;
import com.hy.demo.Domain.Community.Entity.Community;
import com.hy.demo.Domain.User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long>, CommunityRepositoryCustom {

    Page<CommunityDto> findByCourseIdAndSearch(Long courseId, Pageable pageable, String search, String username);

    Optional<Community> findByIdAndUserId(Long id,Long userId);

    Long deleteByIdAndUserId(Long id, Long userId);
}
