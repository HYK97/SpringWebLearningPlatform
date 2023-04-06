package com.hy.demo.domain.community.controller;

import com.hy.demo.config.auth.PrincipalDetails;
import com.hy.demo.domain.community.dto.CommunityDto;
import com.hy.demo.domain.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

import static com.hy.demo.enumcode.AJAXResponseCode.FAIL;
import static com.hy.demo.enumcode.AJAXResponseCode.OK;

@Controller
@RequestMapping("/community/*")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;


    /**
     * 게시글 view 데이터 전송 메서드
     * 10개씩 검색 혹은 paging 데이터 ajax 콜백
     */
    @ResponseBody
    @PostMapping("getCommunityList/{courseId}")
    public Page<CommunityDto> getCommunityList(@PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long courseId, @RequestParam(defaultValue = "") @Length(max = 200, message = "200자 이하로 입력해주세요") String search) {
        return communityService.findCommunityList(courseId, search, pageable, null);
    }

    ;

    @ResponseBody
    @PostMapping("myCommunityList/{courseId}")
    public Page<CommunityDto> myCommunityList(@AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long courseId, @RequestParam(defaultValue = "") @Length(max = 200, message = "200자 이하로 입력해주세요") String search) {
        return communityService.findCommunityList(courseId, search, pageable, principalDetails.getUser());
    }

    ;

    @ResponseBody
    @PostMapping("createCommunity/{courseId}")
    public CommunityDto createCommunity(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long courseId, @Validated @ModelAttribute("data") CommunityDto community) {
        try {
            return communityService.addCommunity(community.toEntity(), principalDetails.getUser(), courseId);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    ;

    @ResponseBody
    @PostMapping("modifyCommunity/{communityId}")
    public CommunityDto modifyCommunity(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long communityId, @Validated @ModelAttribute("data") CommunityDto updateCommunity) {
        CommunityDto communityDto = communityService.modifyCommunity(communityId, updateCommunity.toEntity(), principalDetails.getUser());
        communityDto.setUser(principalDetails.getUser().changeDto());
        communityDto.setMyCommunity(1);
        return communityDto;
    }

    ;

    @ResponseBody
    @PostMapping("deleteCommunity/{communityId}")
    public String deleteCommunity(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long communityId) {

        try {
            communityService.deleteCommunity(principalDetails.getUser(), communityId);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            return FAIL.toString(); //실패
        }
        return OK.toString(); //성공
    }

    ;


    @ResponseBody
    @PostMapping("getCommunity/{communityId}")
    public CommunityDto getCommunity(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long communityId) {
        CommunityDto community = communityService.findCommunity(communityId);
        if (principalDetails.getUsername().equals(community.getUser().getUsername())) {
            community.setMyCommunity(1);
        } else {
            community.setMyCommunity(null);
        }
        return community;
    }

    ;


}