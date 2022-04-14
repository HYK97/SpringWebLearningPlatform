package com.hy.demo.Domain.Community.Controller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Community.Dto.CommunityDto;
import com.hy.demo.Domain.Community.Service.CommunityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/community/*")
public class CommunityController {

    private CommunityService communityService;

    @ResponseBody
    @PostMapping("getCommunityList/{courseId}")
    public Page<CommunityDto> getCommunityList(@PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long courseId, @RequestParam(defaultValue = "") String search){
        return communityService.findCommunityList(courseId, search, pageable);
    };


}
