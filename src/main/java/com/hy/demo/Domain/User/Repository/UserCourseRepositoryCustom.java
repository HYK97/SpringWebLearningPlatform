package com.hy.demo.Domain.User.Repository;

import com.hy.demo.Domain.User.Dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserCourseRepositoryCustom {
    Long countDateRegisteredUserCountByCourseId(Long courseId, String date);

    Map countMonthlyToDayRegisteredUserByCourseId(Long courseId, String date);

    Map countThisYearToMonthlyRegisteredUserByCourseId(Long courseId, String date);

    Map countTenYearToYearRegisteredUserByCourseId(Long courseId, String date);

    void nativeQuery(Long courseId);

    List<UserDto> findRankRandomUserById(int amount);
}
