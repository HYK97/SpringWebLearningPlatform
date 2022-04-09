package com.hy.demo.Domain.User.Repository;

import com.hy.demo.Domain.User.Dto.UserDto;
import com.hy.demo.Domain.User.Entity.UserCourse;
import com.hy.demo.Utils.DateFormatter;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.hy.demo.Domain.Course.Entity.QCourse.course;
import static com.hy.demo.Domain.User.Entity.QUser.user;
import static com.hy.demo.Domain.User.Entity.QUserCourse.userCourse;


public class UserCourseRepositoryImpl extends QueryDsl4RepositorySupport implements UserCourseRepositoryCustom {


    public UserCourseRepositoryImpl() {
        super(UserCourse.class);
    }


    @Autowired
    Logger logger;


    public Long countDateRegisteredUserCountByCourseId(Long courseId, String date) {

        DateFormatter localDateParser = new DateFormatter(date);
        return select(userCourse.user.count())
                .from(userCourse)
                .where(userCourse.createDate.between(localDateParser.startDate(), localDateParser.endDate()).and(course.id.eq(courseId)))
                .fetchOne();
    }


    public Map countMonthlyToDayRegisteredUserByCourseId(Long courseId, String date) {

        DateFormatter localDateParser = new DateFormatter(date);
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
        DateFormatter localDateParser = new DateFormatter(date);
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
            map.put(localDateParser.getYear() + "-" + String.format("%02d", i), 0L);
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
        DateFormatter localDateParser = new DateFormatter(date);
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
        for (int i = localDateParser.getYear() - 10; i <= localDateParser.getYear(); i++) {
            map.put(String.format("%04d", i), 0L);
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

    // TODO 누적쿼리 만들기

    /**
     * 참고 쿼리
     * 4월 누적통계
     * <p>
     * SELECT *
     * from
     * (select
     * FORMATDATETIME(u2.create_date,
     * 'Y-MM-dd')  as create_date,
     * (select
     * count(*)
     * from
     * USER_COURSE u1
     * where
     * FORMATDATETIME(u1.create_date,'Y-MM-dd') between FORMATDATETIME(c.create_date,'Y-MM-dd')  and FORMATDATETIME(u2.create_date,'Y-MM-dd')
     * and u1.course_id =10) as counts
     * from
     * USER_COURSE u2
     * left join
     * course c
     * on u2.course_id =c.course_id
     * )
     * where CREATE_DATE between  FORMATDATETIME('2022-04-01','Y-MM-dd')  and FORMATDATETIME('2022-04-22','Y-MM-dd')
     * group by
     * create_date
     * order by
     * create_date asc;
     */
    public void nativeQuery(Long courseId) {
        EntityManager em = getEntityManager();
        Query nativeQuery = em.createNativeQuery("" +
                "select * from(" +
                "select " +
                "FORMATDATETIME(u2.create_date,'Y-MM')  as create_date, " +
                "(select count(*)  " +
                "from  " +
                "USER_COURSE u1 " +
                "      where FORMATDATETIME(u1.create_date,'Y-MM') between FORMATDATETIME(c.create_date,'Y-MM')  and FORMATDATETIME(u2.create_date,'Y-MM') and u1.course_id =?) as counts " +
                "from USER_COURSE u2 " +
                "left join course c " +
                "on u2.course_id =c.course_id)" +
                "group by create_date " +
                "order by create_date asc; ")
                .setParameter(1, courseId);

        List<Object[]> resultList = nativeQuery.getResultList();
        for (Object[] row : resultList) {
            logger.info("id = " + row[0]);
            logger.info("age = " + row[1]);
        }
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
  /*  StringTemplate dayCOURSE Format = Expressions.stringTemplate(
            "DATE_FORMAT({0}, '%Y-%m-%d')"
            , userCourse.createDate);


    StringTemplate monthFormat = Expressions.stringTemplate(
            "DATE_FORMAT({0}, '%Y-%m')"
            , userCourse.createDate);

    StringTemplate yearFormat = Expressions.stringTemplate(
            "DATE_FORMAT({0}, '%Y')"
            , userCourse.createDate);*/



    //수강자수 가장 많이 보유한 강사순
    public List<UserDto> findRankRandomUserById(int amount) {
        return select(Projections.constructor(UserDto.class,
                user.id,
                user.username,
                user.profileImage,
                user.selfIntroduction
        ))
                .from(userCourse)
                .leftJoin(userCourse.course,course)
                .leftJoin(course.user,user)
                //h2
                .groupBy(user.id)
                .orderBy(user.id.count().desc(), NumberExpression.random().desc())
                //mysql
                //.orderBy(user.id.count().desc(),Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(amount)
                .fetch();
    }



}
