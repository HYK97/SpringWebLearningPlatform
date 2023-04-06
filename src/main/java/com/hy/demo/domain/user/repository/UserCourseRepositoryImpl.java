package com.hy.demo.domain.user.repository;

import com.hy.demo.domain.user.dto.UserDto;
import com.hy.demo.domain.user.entity.UserCourse;
import com.hy.demo.utils.DateFormat;
import com.hy.demo.utils.DateParser;
import com.hy.demo.utils.QueryDsl4RepositorySupport;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.hy.demo.domain.course.entity.QCourse.course;
import static com.hy.demo.domain.user.entity.QUser.user;
import static com.hy.demo.domain.user.entity.QUserCourse.userCourse;


@Slf4j
public class UserCourseRepositoryImpl extends QueryDsl4RepositorySupport implements UserCourseRepositoryCustom {

    private final DateParser dateParser;
    private final DateFormat dateFormat;

    // TODO 누적쿼리 만들기

    @Autowired
    public UserCourseRepositoryImpl(DateParser dateParser, DateFormat dateFormat) {
        super(UserCourse.class);
        this.dateParser = dateParser;
        this.dateFormat = dateFormat;
    }

    public Long countDateRegisteredUserCountByCourseId(Long courseId, String date) {

        return select(userCourse.user.count())
                .from(userCourse)
                .where(userCourse.createDate.between(dateParser.startDate(date), dateParser.endDate(date)).and(course.id.eq(courseId)))
                .fetchOne();
    }

    public Map countMonthlyToDayRegisteredUserByCourseId(Long courseId, String date) {


        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory.select(dateFormat.getDayFormat(userCourse.createDate).count(), dateFormat.getDayFormat(userCourse.createDate))
                .from(userCourse)
                .where(userCourse.createDate.between(dateParser.startMonth(date), dateParser.endMonth(date)).and(userCourse.course.id.eq(courseId)))
                .groupBy(dateFormat.getDayFormat(userCourse.createDate))
                .orderBy(dateFormat.getDayFormat(userCourse.createDate).asc())
                .fetch();
        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = 1; i <= dateParser.endDay(date); i++) {
            map.put(dateParser.getYear(date) + "-" + dateParser.getMonth(date) + "-" + String.format("%02d", i), 0L);
        }
        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(dateFormat.getDayFormat(userCourse.createDate)))) {
                    map.replace(key, tuple.get(dateFormat.getDayFormat(userCourse.createDate).count()));
                }
            }
        }
        return map;

    }

    public Map countThisYearToMonthlyRegisteredUserByCourseId(Long courseId, String date) {

        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory.select(
                        dateFormat.getMonthFormat(userCourse.createDate).count(),
                        dateFormat.getMonthFormat(userCourse.createDate)
                )
                .from(userCourse)
                .where(userCourse.createDate.between(dateParser.thisYearStart(date), dateParser.thisYearEnd(date))
                        .and(userCourse.course.id.eq(courseId)))
                .groupBy(dateFormat.getMonthFormat(userCourse.createDate))
                .orderBy(dateFormat.getMonthFormat(userCourse.createDate).asc())
                .fetch();

        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            map.put(dateParser.getYear(date) + "-" + String.format("%02d", i), 0L);
        }

        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(dateFormat.getMonthFormat(userCourse.createDate)))) {
                    map.replace(key, tuple.get(dateFormat.getMonthFormat(userCourse.createDate).count()));
                }
            }
        }

        return map;

    }

    public Map countTenYearToYearRegisteredUserByCourseId(Long courseId, String date) {

        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory.select(
                        dateFormat.getYearFormat(userCourse.createDate).count(),
                        dateFormat.getYearFormat(userCourse.createDate)
                )
                .from(userCourse)
                .where(userCourse.createDate.between(dateParser.tenYearAgo(date), dateParser.thisYearEnd(date))
                        .and(userCourse.course.id.eq(courseId)))
                .groupBy(dateFormat.getYearFormat(userCourse.createDate))
                .orderBy(dateFormat.getYearFormat(userCourse.createDate).asc())
                .fetch();

        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = dateParser.getYear(date) - 10; i <= dateParser.getYear(date); i++) {
            map.put(String.format("%04d", i), 0L);
        }

        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(dateFormat.getYearFormat(userCourse.createDate)))) {
                    map.replace(key, tuple.get(dateFormat.getYearFormat(userCourse.createDate).count()));
                }
            }
        }

        return map;

    }

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
            log.debug("id = {}", row[0]);
            log.debug("age = {}", row[1]);
        }
    }

    //수강자수 가장 많이 보유한 강사순
    public List<UserDto> findRankRandomUserById(int amount) {
        return select(Projections.constructor(UserDto.class,
                user.id,
                user.username,
                user.profileImage,
                user.selfIntroduction,
                user.nickname
        ))
                .from(userCourse)
                .leftJoin(userCourse.course, course)
                .leftJoin(course.user, user)
                .groupBy(user.id)
                //h2
                //.orderBy(user.id.count().desc(), NumberExpression.random().desc())
                //mysql
                .orderBy(user.id.count().desc(), Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(amount)
                .fetch();
    }


}
