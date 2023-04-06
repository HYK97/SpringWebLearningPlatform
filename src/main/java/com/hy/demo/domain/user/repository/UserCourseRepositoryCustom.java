package com.hy.demo.domain.user.repository;

import com.hy.demo.domain.user.dto.UserDto;

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
