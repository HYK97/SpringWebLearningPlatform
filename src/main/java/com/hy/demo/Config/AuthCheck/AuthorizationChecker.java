package com.hy.demo.Config.AuthCheck;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Board.Repository.CourseBoardRepository;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.File.Entity.File;
import com.hy.demo.Domain.File.Repository.FileRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserCourseRepository;
import com.hy.demo.Domain.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class AuthorizationChecker {



    private final UserCourseRepository userCourseRepository;


    private final FileRepository fileRepository;


    private final CourseRepository courseRepository;


    private final CourseBoardRepository courseBoardRepository;


    private final UserRepository userRepository;


    private final Logger logger;

    @Transactional
    public boolean isAccessBoard(Long courseId) throws AccessDeniedException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        final User findUser = userRepository.findByUsername(principalDetails.getUser().getUsername());
        userCourseRepository.findByUserAndCourseId(findUser, courseId)
                .orElseThrow(() -> new AccessDeniedException("수강신청 안함", "1", "1"));
        return true;
    }

    @Transactional
    public boolean isManagementBoard(Long courseId) throws AccessDeniedException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        final User findUser = userRepository.findByUsername(principalDetails.getUser().getUsername());
        courseRepository.findByUserAndId(findUser, courseId)
                .orElseThrow(() -> new AccessDeniedException("수강관리 접근권한에러", "2", "2"));
        return true;
    }


    @Transactional
    public boolean isFile(Long fileId, Long courseId, Long courseBoardId) throws AccessDeniedException, FileNotFoundException {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        final User findUser = userRepository.findByUsername(principalDetails.getUser().getUsername());

        if (findUser.getRole().equals("ROLE_USER")) {
            userCourseRepository.findByUserAndCourseId(findUser, courseId)
                    .orElseThrow(() -> new AccessDeniedException("권한 없음"));
        } else {
            courseRepository.findByUserAndCourseId(findUser, courseId)
                    .orElseThrow(() -> new AccessDeniedException("권한 없음"));
        }
        courseBoardRepository.findByCourseId(courseId)
                .orElseThrow(() -> new AccessDeniedException("잘못된 코스"));

        final File findFile = fileRepository.findFetchById(fileId)
                .orElseThrow(() -> new FileNotFoundException("찾는 파일 없음"));
        final Long findCourseId = findFile.getCourseBoard().getId();
        if (findCourseId != courseBoardId) {
            throw new AccessDeniedException("잘못된 파일요청");
        }

        return true;
    }


}
