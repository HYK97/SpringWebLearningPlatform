package com.hy.demo.Domain.Comments.Repository;

import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.Comments.Entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long>,CommentsRepositoryCustom{
    Page<CommentsDto> findByCourseBoardId(Long courseBoardId, Pageable pageable);
    Page<CommentsDto> findReplyByIds(Long id, Pageable pageable);
}
