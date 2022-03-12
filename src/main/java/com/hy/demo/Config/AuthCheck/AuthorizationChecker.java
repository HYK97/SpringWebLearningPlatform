package com.hy.demo.Config.AuthCheck;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserCourseRepository;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;

@Component
public class AuthorizationChecker {

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public boolean isAccessBoard(Long courseId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        User findUser = userRepository.findByUsername(principalDetails.getUser().getUsername());
        userCourseRepository.findByUserAndCourseId(findUser, courseId)
                .orElseThrow(()-> new AccessDeniedException("찾는엔티티없음"));
        return true;
    }


}
