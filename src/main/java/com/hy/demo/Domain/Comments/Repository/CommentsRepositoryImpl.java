package com.hy.demo.Domain.Comments.Repository;

import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.Comments.Entity.Comments;
import com.hy.demo.Domain.Comments.Entity.QComments;
import com.hy.demo.Utils.DateFormatter;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hy.demo.Domain.Board.Entity.QCourseBoard.courseBoard;
import static com.hy.demo.Domain.Comments.Entity.QComments.comments1;
import static com.hy.demo.Domain.Course.Entity.QCourse.course;
import static com.hy.demo.Domain.User.Entity.QUser.user;


public class CommentsRepositoryImpl extends QueryDsl4RepositorySupport implements CommentsRepositoryCustom {


    public CommentsRepositoryImpl() {
        super(Comments.class);
    }


    @Autowired
    Logger logger;


    @Override
    public Page<CommentsDto> findByCourseBoardId(Long courseBoardId, Pageable pageable) {
        QComments reply = new QComments("reply");
        return
                applyPagination(pageable, query ->
                        query.select(Projections.constructor(CommentsDto.class,
                                comments1.id,
                                user.username,
                                comments1.comments,
                                comments1.createDate,
                                select(reply.count()).from(reply).where(comments1.id.eq(reply.parent.id)),
                                user.profileImage
                        ))
                                .from(comments1)
                                .leftJoin(comments1.user, user)
                                .where(comments1.courseBoard.id.eq(courseBoardId), comments1.parent.isNull())
                );

    }


    public Page<CommentsDto> findReplyByIds(Long id, Pageable pageable) {
        return applyPagination(pageable, query ->
                query.select(Projections.constructor(CommentsDto.class,
                        user.username,
                        comments1.id,
                        comments1.comments,
                        comments1.createDate,
                        comments1.parent.id,
                        user.profileImage
                ))
                        .from(comments1)
                        .where(comments1.parent.id.eq(id)));
    }

    public Optional<Comments> findByIdAndUser(Long id, String username) {
        return Optional.ofNullable(select(comments1)
                .from(comments1)
                .leftJoin(comments1.user, user)
                .where(comments1.id.eq(id).and(comments1.user.username.eq(username))).fetchOne());

    }

    public Long countDateCommentCountByCourseId(Long courseId, String date) {

        DateFormatter localDateParser = new DateFormatter(date);
        return select(comments1.count())
                .from(comments1)
                .leftJoin(comments1.courseBoard, courseBoard)
                .leftJoin(courseBoard.course, course)
                .where(course.id.eq(courseId).and(comments1.createDate.between(localDateParser.startDate(), localDateParser.endDate())))
                .fetchOne();
    }








    public Map countMonthlyToDayCommentsByCourseId(Long courseId, String date) {
        DateFormatter localDateParser = new DateFormatter(date);
        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory
                .select(comments1.count(), dayFormat)
                .from(comments1)
                .leftJoin(comments1.courseBoard, courseBoard)
                .leftJoin(courseBoard.course, course)
                .where(comments1.createDate.between(localDateParser.startMonth(), localDateParser.endMonth()).and(course.id.eq(courseId)))
                .groupBy(dayFormat)
                .having()
                .orderBy(dayFormat.asc())
                .fetch();
        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = 1; i <= localDateParser.endDay(); i++) {
            map.put(localDateParser.getYear() + "-" + localDateParser.getMonth() + "-" + String.format("%02d", i), 0L);
        }
        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(dayFormat))) {
                    map.replace(key, tuple.get(comments1.count()));
                }
            }
        }
        return map;
    }


    public Map countThisYearToMonthlyCommentsByCourseId(Long courseId, String date) {
        DateFormatter localDateParser = new DateFormatter(date);
        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory
                .select(comments1.count(), monthFormat)
                .from(comments1)
                .leftJoin(comments1.courseBoard, courseBoard)
                .leftJoin(courseBoard.course, course)
                .where(comments1.createDate.between(localDateParser.thisYearStart(), localDateParser.thisYearEnd()))
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
                    map.replace(key, tuple.get(comments1.count()));
                }
            }
        }

        return map;

    }

    public Map countTenYearToYearCommentsByCourseId(Long courseId, String date) {
        DateFormatter localDateParser = new DateFormatter(date);
        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory.select(comments1.count(), yearFormat)
                .from(comments1)
                .leftJoin(comments1.courseBoard, courseBoard)
                .leftJoin(courseBoard.course, course)
                .where(comments1.createDate.between(localDateParser.tenYearAgo(), localDateParser.thisYearEnd()).and(course.id.eq(courseId)))
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
                    map.replace(key, tuple.get(comments1.count()));
                }
            }
        }

        return map;

    }

    //h2
    StringTemplate dayFormat = Expressions.stringTemplate(
            "FORMATDATETIME({0}, 'Y-MM-dd')"
            , comments1.createDate);

    StringTemplate monthFormat = Expressions.stringTemplate(
            "FORMATDATETIME({0}, 'Y-MM')"
            , comments1.createDate);

    StringTemplate yearFormat = Expressions.stringTemplate(
            "FORMATDATETIME({0}, 'Y')"
            , comments1.createDate);

    //mysql
  /* StringTemplate dayFormat = Expressions.stringTemplate(
            "DATE_FORMAT({0}, '%Y-%m-%d')"
            , comments1.createDate);


    StringTemplate monthFormat = Expressions.stringTemplate(
            "DATE_FORMAT({0}, '%Y-%m-%d')"
            , comments1.createDate);

    StringTemplate yearFormat = Expressions.stringTemplate(
            "DATE_FORMAT({0}, 'Y')"
            , comments1.createDate);*/







}
