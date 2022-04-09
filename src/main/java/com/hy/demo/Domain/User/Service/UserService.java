package com.hy.demo.Domain.User.Service;

import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.User.Dto.UserDto;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Entity.UserCourse;
import com.hy.demo.Domain.User.Repository.UserCourseRepository;
import com.hy.demo.Domain.User.Repository.UserRepository;
import com.hy.demo.Utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCourseRepository userCourseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CourseRepository courseRepository;


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Transactional
    public boolean register(User user, User provider) {
        User byUsername;
        boolean empty;
        if (provider == null) {
            byUsername = userRepository.findByUsername(user.getUsername());
            empty = ObjectUtils.isEmpty(byUsername);
            if (empty) { // 회원없을시
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                logger.info("user.getPassword() = " + user.getPassword());
                userRepository.save(user);
                return true;
            } else {   //회원있을시에
                return false;
            }

        } else {
            byUsername = userRepository.findByUsername(user.getUsername());
            empty = ObjectUtils.isEmpty(byUsername);
            if (empty) { // 회원없을시

                User user2 = User.builder()
                        .username(provider.getUsername())
                        .password(provider.getPassword())
                        .email(provider.getEmail())
                        .role(user.getRole())
                        .provider(provider.getProvider())
                        .providerId(provider.getProviderId())
                        .build();
                logger.info("user.getPassword() = " + user2.getPassword());

                userRepository.save(user2);
                return true;
            } else {   //회원있을시에
                return false;
            }

        }


    }


    public Boolean loginForm(User user) {


        String username = user.getUsername();
        User findUser = userRepository.findByUsername(username);
        if (findUser != null) {
            logger.info("회원");
            return true;
        } else {
            logger.info("비회원");
            return false;
        }
    }

    public User findByUsername(User user) {
        return userRepository.findByUsername(user.getUsername());
    }


    public User findProfileImageByUser(User user) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername())).orElseThrow(() -> new EntityNotFoundException("이미지없음"));

        return findUser;
    }

    public void updateUserProfileImage(String profileImage, User user) {
        User findUser = userRepository.findByUsername(user.getUsername());
        findUser.updateProfileImage(profileImage);
        userRepository.save(findUser);
    }

    public Map findByUserCourse(String course, User user) {
        Long Lid = Long.parseLong(course);
        logger.info("idss= " + Lid);
        CourseDto courseDto = courseService.findDetailCourse(Lid);
        User findUser = findByUsername(user);
        UserCourse findUserCourse = userCourseRepository.findByUserAndCourse(findUser, courseDto.returnEntity());
        Map map = new HashMap();
        map.put("course", courseDto);
        map.put("userCourse", findUserCourse);
        return map;

    }

    public UserDto findUserInfo(User user) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername())).orElseThrow(() -> new EntityNotFoundException("권한없음"));

        logger.info("findUser.changeDto().toString() = " + findUser.changeDto().toString());
        return findUser.changeDto();

    }


    public void application(Long id, String usernames) {


        User username = userRepository.findByUsername(usernames);
        Optional<Course> course = courseRepository.findById(id);
        UserCourse userCourse = UserCourse.builder()
                .user(username)
                .course(course.orElseThrow(IllegalArgumentException::new))
                .build();
        userCourseRepository.save(userCourse);

    }

    public UserDto userUpdate(User user, UserDto update) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername())).orElseThrow(() -> new EntityNotFoundException("권한없음"));
        findUser.updateEmail(update.getEmail());
        findUser.updateSelfIntroduction(update.getSelfIntroduction());
        User updateUser = userRepository.save(findUser);
        return updateUser.changeDto();

    }


    public UserDto passwordUpdate(User user, String password, String newPassword) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername())).orElseThrow(() -> new EntityNotFoundException("권한없음"));


        if (!bCryptPasswordEncoder.matches(password, findUser.getPassword())) {
            throw new AccessDeniedException("권한없음");
        }
        findUser.updatePassword(bCryptPasswordEncoder.encode(newPassword));
        User updateUser = userRepository.save(findUser);
        return updateUser.changeDto();

    }


    public Long countDateRegisteredUserCount(Long courseId, String date) {
        return userCourseRepository.countDateRegisteredUserCountByCourseId(courseId, date);
    }

    public Map countMonthlyToDayRegisteredUser(Long courseId, String date) {
        return userCourseRepository.countMonthlyToDayRegisteredUserByCourseId(courseId, date);
    }

    public Map countThisYearToMonthlyRegisteredUser(Long courseId, String date) {
        return userCourseRepository.countThisYearToMonthlyRegisteredUserByCourseId(courseId, date);
    }
    public Map countTenYearToYearRegisteredUser(Long courseId, String date) {
        return userCourseRepository.countTenYearToYearRegisteredUserByCourseId(courseId, date);
    }

}
