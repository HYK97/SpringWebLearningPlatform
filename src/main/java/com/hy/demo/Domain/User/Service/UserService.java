package com.hy.demo.Domain.User.Service;

import com.hy.demo.Domain.User.Dto.UserDto;
import com.hy.demo.Domain.User.Entity.User;
import org.springframework.dao.DuplicateKeyException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface UserService {
    @Transactional
    void register(UserDto user, User provider) throws DuplicateKeyException;

    Boolean loginForm(User user);

    User findByUsername(String username);

    User findProfileImageByUser(User user);

    void updateUserProfileImage(String profileImage, User user);

    Map findByUserCourse(String course, User user);

    UserDto findUserInfo(String username);

    void application(Long id, String usernames);

    UserDto userUpdate(User user, UserDto update);

    UserDto passwordUpdate(User user, String password, String newPassword);

    Long countDateRegisteredUserCount(Long courseId, String date);

    Map countMonthlyToDayRegisteredUser(Long courseId, String date);

    Map countThisYearToMonthlyRegisteredUser(Long courseId, String date);

    Map countTenYearToYearRegisteredUser(Long courseId, String date);

    List<UserDto> RankRandomUser(int amount);

    User findEmailAndUsername(UserDto user);

    List<UserDto> rankRandomUser(int amount);

    @Transactional
    void courseWithdrawal(String username, Long courseId);
}
