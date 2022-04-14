package com.hy.demo.Domain.Community.Repository;

import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.Community.Dto.CommunityDto;
import com.hy.demo.Domain.Community.Entity.Community;
import com.hy.demo.Domain.Community.Entity.QCommunity;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.hy.demo.Domain.Comments.Entity.QComments.comments1;
import static com.hy.demo.Domain.Community.Entity.QCommunity.community;
import static com.hy.demo.Domain.Course.Entity.QCourse.course;
import static com.hy.demo.Domain.User.Entity.QUser.user;


public class CommunityRepositoryImpl extends QueryDsl4RepositorySupport implements CommunityRepositoryCustom {


    public CommunityRepositoryImpl() {
        super(Community.class);
    }

    public Page<CommunityDto> findByCourseIdAndSearch(Long courseId, Pageable pageable,String search) {
        return applyPagination(pageable, query ->
                query.select(Projections.constructor(CommunityDto.class,
                        community.id,
                        community.user,
                        community.title,
                        community.contents,
                        community.createDate
                ))
                        .from(community)
                        .leftJoin(community.user, user)
                        .where(community.course.id.eq(courseId),searchContains(search))
        );
    }

    private BooleanExpression searchContains(String search) {
        return search != null ? community.title.contains(search).or(community.contents.contains(search)) : null;
    }


}
