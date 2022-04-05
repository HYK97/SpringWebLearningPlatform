package com.hy.demo.Domain.User.Repository;

import com.hy.demo.Domain.User.Entity.UserCourse;
import com.hy.demo.Utils.DateFormater;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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


    public Map countMonthlyRegisteredUserByCourseId(Long courseId, String date) {

        DateFormater localDateParser = new DateFormater(date, "d");
        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory.select(dayFormat.count(), dayFormat)
                .from(userCourse)
                .where(userCourse.createDate.between(localDateParser.startMonth(), localDateParser.endMonth()).and(course.id.eq(courseId)))
                .groupBy(dayFormat)
                .orderBy(dayFormat.asc())
                .fetch();
        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = 1; i <= localDateParser.endDay(); i++) {
            map.put(localDateParser.getYear() + "-" + localDateParser.getMonth() + "-" + String.format("%02d", i), 0L);
        }
        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(dayFormat))) {
                    map.replace(key, tuple.get(dayFormat.count()));
                }
            }
        }

        return map;

    }


    public Map countThisYearToMonthlyRegisteredUserByCourseId(Long courseId, String date) {
        DateFormater localDateParser = new DateFormater(date, "d");
        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory.select(monthFormat.count(), monthFormat)
                .from(userCourse)
                .where(userCourse.createDate.between(localDateParser.thisYearStart(), localDateParser.thisYearEnd()).and(course.id.eq(courseId)))
                .groupBy(monthFormat)
                .orderBy(monthFormat.asc())
                .fetch();
        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            map.put(localDateParser.getYear() + "-" + String.format("%02d", i), 0L);
        }
        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(monthFormat))) {
                    logger.info("tuple = " + tuple);
                    logger.info("key = " + key);
                    map.replace(key, tuple.get(monthFormat.count()));
                }
            }
        }

        return map;

    }


    //h2
    StringTemplate dayFormat = Expressions.stringTemplate(
            "FORMATDATETIME({0}, {1})"
            , userCourse.createDate
            , ConstantImpl.create("Y-MM-dd"));

    StringTemplate monthFormat = Expressions.stringTemplate(
            "FORMATDATETIME({0}, {1})"
            , userCourse.createDate
            , ConstantImpl.create("Y-MM"));

    //mysql
   /* StringTemplate dayFormat = Expressions.stringTemplate(
            "DATE_FORMAT({0}, {1})"
            , userCourse.createDate
            , ConstantImpl.create("%Y-%m-%d"));*/


}
