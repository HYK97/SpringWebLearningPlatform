package com.hy.demo.Domain.User.Repository;

public interface UserCourseRepositoryCustom {
    Long countDateRegisteredUserCountByCourseId(Long courseId,String date);
}
