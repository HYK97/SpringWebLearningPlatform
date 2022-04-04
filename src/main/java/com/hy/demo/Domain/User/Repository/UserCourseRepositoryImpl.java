package com.hy.demo.Domain.User.Repository;

import com.hy.demo.Domain.User.Entity.UserCourse;
import com.hy.demo.Utils.DateFormater;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static com.hy.demo.Domain.Course.Entity.QCourse.course;
import static com.hy.demo.Domain.User.Entity.QUserCourse.userCourse;


public class UserCourseRepositoryImpl extends QueryDsl4RepositorySupport implements UserCourseRepositoryCustom {


    public UserCourseRepositoryImpl() {
        super(UserCourse.class);
    }


    @Autowired
    Logger logger;


    public Long countDateRegisteredUserCountByCourseId(Long courseId, String date) {

        DateFormater localDateParser = new DateFormater(date, "d");
        return select(userCourse.user.count())
                .from(userCourse)
                .where(userCourse.createDate.between(localDateParser.startDate(), localDateParser.endDate()).and(course.id.eq(courseId)))
                .fetchOne();
    }


}
