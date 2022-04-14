package com.hy.demo.Domain.Community.Repository;

import com.hy.demo.Domain.Community.Dto.CommunityDto;
import com.hy.demo.Domain.Community.Entity.Community;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.hy.demo.Domain.Community.Entity.QCommunity.community;
import static com.hy.demo.Domain.User.Entity.QUser.user;


public class CommunityRepositoryImpl extends QueryDsl4RepositorySupport implements CommunityRepositoryCustom {


    public CommunityRepositoryImpl() {
        super(Community.class);
    }

    public Page<CommunityDto> findByCourseIdAndSearch(Long courseId, Pageable pageable, String search, String username) {
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
                        .where(community.course.id.eq(courseId), searchContains(search), usernameEq(username))
        );
    }

    private BooleanExpression searchContains(String search) {
        return search != null ? community.title.contains(search).or(community.contents.contains(search)) : null;
    }

    private BooleanExpression usernameEq(String username) {
        return username != null ? community.user.username.eq(username) : null;
    }

    public CommunityDto findDtoById(Long courseId) {
        return select(Projections.constructor(CommunityDto.class,
                community.id,
                community.user,
                community.title,
                community.contents,
                community.createDate
        ))
                .from(community)
                .where(community.id.eq(courseId))
                .fetchOne();
    }


}
