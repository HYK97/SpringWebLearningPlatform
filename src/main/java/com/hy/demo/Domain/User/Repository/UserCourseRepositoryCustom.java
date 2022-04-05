package com.hy.demo.Domain.User.Repository;

import java.util.Map;

public interface UserCourseRepositoryCustom {
    Long countDateRegisteredUserCountByCourseId(Long courseId, String date);

    Map countMonthlyRegisteredUserByCourseId(Long courseId, String date);

    Map countThisYearToMonthlyRegisteredUserByCourseId(Long courseId, String date);
}
