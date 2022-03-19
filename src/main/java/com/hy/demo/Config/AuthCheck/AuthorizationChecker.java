package com.hy.demo.Config.AuthCheck;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Repository.CourseBoardRepository;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.File.Entity.File;
import com.hy.demo.Domain.File.Repository.FileRepository;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserCourseRepository;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Component
public class AuthorizationChecker {

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseBoardRepository courseBoardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Logger logger;

    @Transactional
    public boolean isAccessBoard(Long courseId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        User findUser = userRepository.findByUsername(principalDetails.getUser().getUsername());
        userCourseRepository.findByUserAndCourseId(findUser, courseId)
                .orElseThrow(() -> new AccessDeniedException("수강신청 안함", "1", "1"));
        return true;
    }

    @Transactional
    public boolean isManagementBoard(Long courseId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User findUser = userRepository.findByUsername(principalDetails.getUser().getUsername());
        courseRepository.findByUserAndId(findUser, courseId)
                .orElseThrow(() -> new AccessDeniedException("수강관리 접근권한에러", "2", "2"));
        return true;
    }


    @Transactional
    public boolean isFile(Long fileId, Long courseId, Long courseBoardId) throws AccessDeniedException, FileNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User findUser = userRepository.findByUsername(principalDetails.getUser().getUsername());

        if (findUser.getRole().equals("ROLE_USER")) {
            userCourseRepository.findByUserAndCourseId(findUser, courseId)
                    .orElseThrow(() -> new AccessDeniedException("권한 없음"));
        } else {
            courseRepository.findByUserAndCourseId(findUser, courseId)
                    .orElseThrow(() -> new AccessDeniedException("권한 없음"));
        }
        courseBoardRepository.findByCourseId(courseId)
                .orElseThrow(() -> new AccessDeniedException("잘못된 코스"));

        File findFile = fileRepository.findFetchById(fileId)
                .orElseThrow(() -> new FileNotFoundException("찾는 파일 없음"));
        Long findCourseId = findFile.getCourseBoard().getId();
        if (findCourseId != courseBoardId) {
            throw new AccessDeniedException("잘못된 파일요청");
        }

        return true;
    }


}
