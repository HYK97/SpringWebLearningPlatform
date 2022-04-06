package com.hy.demo.Domain.User.Repository;

import com.hy.demo.Domain.User.Entity.UserCourse;
import com.hy.demo.Utils.DateFormater;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.Tuple;
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

        DateFormater localDateParser = new DateFormater(date);
        return select(userCourse.user.count())
                .from(userCourse)
                .where(userCourse.createDate.between(localDateParser.startDate(), localDateParser.endDate()).and(course.id.eq(courseId)))
                .fetchOne();
    }


    public Map countMonthlyToDayRegisteredUserByCourseId(Long courseId, String date) {

        DateFormater localDateParser = new DateFormater(date);
        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory.select(dayFormat.count(), dayFormat)
                .from(userCourse)
                .where(userCourse.createDate.between(localDateParser.startMonth(), localDateParser.endMonth()).and(userCourse.course.id.eq(courseId)))
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
        DateFormater localDateParser = new DateFormater(date);
        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory.select(
                monthFormat.count(),
                monthFormat
        )
                .from(userCourse)
                .where(userCourse.createDate.between(localDateParser.thisYearStart(), localDateParser.thisYearEnd())
                        .and(userCourse.course.id.eq(courseId)))
                .groupBy(monthFormat)
                .orderBy(monthFormat.asc())
                .fetch();

        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            map.put(localDateParser.getYear() + "-" + String.format("%02d", i) , 0L);
        }

        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(monthFormat))) {
                    map.replace(key, tuple.get(monthFormat.count()));
                }
            }
        }

        return map;

    }

    public Map countTenYearToYearRegisteredUserByCourseId(Long courseId, String date) {
        DateFormater localDateParser = new DateFormater(date);
        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory.select(
                yearFormat.count(),
                yearFormat
        )
                .from(userCourse)
                .where(userCourse.createDate.between(localDateParser.tenYearAgo(), localDateParser.thisYearEnd())
                        .and(userCourse.course.id.eq(courseId)))
                .groupBy(yearFormat)
                .orderBy(yearFormat.asc())
                .fetch();

        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = localDateParser.getYear()-10; i <= localDateParser.getYear(); i++) {
            map.put(String.format("%04d", i) , 0L);
        }

        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(yearFormat))) {
                    map.replace(key, tuple.get(yearFormat.count()));
                }
            }
        }

        return map;

    }




    //h2
    StringTemplate dayFormat = Expressions.stringTemplate(
            "FORMATDATETIME({0}, 'Y-MM-dd')"
            , userCourse.createDate);

    StringTemplate monthFormat = Expressions.stringTemplate(
            "FORMATDATETIME({0}, 'Y-MM')"
            , userCourse.createDate);

    StringTemplate yearFormat = Expressions.stringTemplate(
            "FORMATDATETIME({0}, 'Y')"
            , userCourse.createDate);

    //mysql
   /* StringTemplate dayFormat = Expressions.stringTemplate(
            "DATE_FORMAT({0}, '%Y-%m-%d')"
            , userCourse.createDate);*/


}
