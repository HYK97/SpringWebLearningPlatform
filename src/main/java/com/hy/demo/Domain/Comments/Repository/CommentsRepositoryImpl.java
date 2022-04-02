package com.hy.demo.Domain.Comments.Repository;

import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.Comments.Entity.Comments;
import com.hy.demo.Domain.Comments.Entity.QComments;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.hy.demo.Domain.Comments.Entity.QComments.comments1;
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

}
