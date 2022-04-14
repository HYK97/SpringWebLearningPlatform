package com.hy.demo.Domain.Community.Service;

import com.hy.demo.Domain.Community.Dto.CommunityDto;
import com.hy.demo.Domain.Community.Entity.Community;
import com.hy.demo.Domain.Community.Repository.CommunityRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * service 명명 규칙
 * select -> find
 * modifyCourseEvaluation -> modify
 * insert -> add
 * delete -> delete
 */

@Service
public class CommunityService {


    @Autowired
    private CommunityRepository communityRepository;


    @Autowired
    Logger logger;

    public Page<CommunityDto> findCommunityList(Long courseId, String search, Pageable pageable) {

        return communityRepository.findByCourseIdAndSearch(courseId,pageable,search);

    }


}
