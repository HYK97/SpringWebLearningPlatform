package com.hy.demo.domain.comments.repository;

import com.hy.demo.domain.comments.dto.CommentsDto;
import com.hy.demo.domain.comments.entity.Comments;
import com.hy.demo.domain.comments.entity.QComments;
import com.hy.demo.utils.DateFormat;
import com.hy.demo.utils.DateParser;
import com.hy.demo.utils.QueryDsl4RepositorySupport;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hy.demo.domain.board.entity.QCourseBoard.courseBoard;
import static com.hy.demo.domain.comments.entity.QComments.comments1;
import static com.hy.demo.domain.course.entity.QCourse.course;
import static com.hy.demo.domain.user.entity.QUser.user;


public class CommentsRepositoryImpl extends QueryDsl4RepositorySupport implements CommentsRepositoryCustom {


    private final DateFormat dateFormat;
    private final DateParser dateParser;


    @Autowired
    public CommentsRepositoryImpl(DateFormat dateFormat, DateParser dateParser) {
        super(Comments.class);
        this.dateFormat = dateFormat;

        this.dateParser = dateParser;
    }


    @Override
    public Page<CommentsDto> findByCourseBoardId(Long id, Pageable pageable, int status) {
        QComments reply = new QComments("reply");
        return
                applyPagination(pageable, query ->
                        query.select(Projections.constructor(CommentsDto.class,
                                        comments1.id,
                                        user,
                                        comments1.comments,
                                        comments1.createDate,
                                        select(reply.count()).from(reply).where(comments1.id.eq(reply.parent.id))
                                ))
                                .from(comments1)
                                .leftJoin(comments1.user, user)
                                .where(checkStatus(status, id), comments1.parent.isNull())
                );
    }

    private BooleanExpression checkStatus(int status, Long id) {
        return status == 1 ? comments1.courseBoard.id.eq(id) : comments1.community.id.eq(id);
    }

    public Page<CommentsDto> findReplyByIds(Long id, Pageable pageable) {
        return applyPagination(pageable, query ->
                query.select(Projections.constructor(CommentsDto.class,
                                comments1.id,
                                comments1.comments,
                                comments1.createDate,
                                comments1.parent.id,
                                user
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


        return select(comments1.count())
                .from(comments1)
                .leftJoin(comments1.courseBoard, courseBoard)
                .leftJoin(courseBoard.course, course)
                .where(course.id.eq(courseId).and(comments1.createDate.between(dateParser.startDate(date), dateParser.endDate(date))))
                .fetchOne();
    }


    public Map countMonthlyToDayCommentsByCourseId(Long courseId, String date) {
        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory
                .select(comments1.count(), dateFormat.getDayFormat(comments1.createDate))
                .from(comments1)
                .leftJoin(comments1.courseBoard, courseBoard)
                .leftJoin(courseBoard.course, course)
                .where(comments1.createDate.between(dateParser.startMonth(date), dateParser.endMonth(date)).and(course.id.eq(courseId)))
                .groupBy(dateFormat.getDayFormat(comments1.createDate))
                .having()
                .orderBy(dateFormat.getDayFormat(comments1.createDate).asc())
                .fetch();
        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = 1; i <= dateParser.endDay(date); i++) {
            map.put(dateParser.getYear(date) + "-" + dateParser.getMonth(date) + "-" + String.format("%02d", i), 0L);
        }
        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(dateFormat.getDayFormat(comments1.createDate)))) {
                    map.replace(key, tuple.get(comments1.count()));
                }
            }
        }
        return map;
    }

    public Map countThisYearToMonthlyCommentsByCourseId(Long courseId, String date) {

        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory
                .select(comments1.count(), dateFormat.getMonthFormat(comments1.createDate))
                .from(comments1)
                .leftJoin(comments1.courseBoard, courseBoard)
                .leftJoin(courseBoard.course, course)
                .where(comments1.createDate.between(dateParser.thisYearStart(date), dateParser.thisYearEnd(date)))
                .groupBy(dateFormat.getMonthFormat(comments1.createDate))
                .orderBy(dateFormat.getMonthFormat(comments1.createDate).asc())
                .fetch();
        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            map.put(dateParser.getYear(date) + "-" + String.format("%02d", i), 0L);
        }

        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(dateFormat.getMonthFormat(comments1.createDate)))) {
                    map.replace(key, tuple.get(comments1.count()));
                }
            }
        }

        return map;

    }

    public Map countTenYearToYearCommentsByCourseId(Long courseId, String date) {

        JPAQueryFactory queryFactory = getQueryFactory();
        List<Tuple> fetch = queryFactory.select(comments1.count(), dateFormat.getYearFormat(comments1.createDate))
                .from(comments1)
                .leftJoin(comments1.courseBoard, courseBoard)
                .leftJoin(courseBoard.course, course)
                .where(comments1.createDate.between(dateParser.tenYearAgo(date), dateParser.thisYearEnd(date)).and(course.id.eq(courseId)))
                .groupBy(dateFormat.getYearFormat(comments1.createDate))
                .orderBy(dateFormat.getYearFormat(comments1.createDate).asc())
                .fetch();

        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = dateParser.getYear(date) - 10; i <= dateParser.getYear(date); i++) {
            map.put(String.format("%04d", i), 0L);
        }
        for (String key : map.keySet()) {
            for (Tuple tuple : fetch) {
                if (key.equals(tuple.get(dateFormat.getYearFormat(comments1.createDate)))) {
                    map.replace(key, tuple.get(comments1.count()));
                }
            }
        }

        return map;

    }


}
