package com.hy.demo.Domain.Comments.Repository;

import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentsRepositoryCustom {
    Page<CommentsDto> findByCourseBoardId(Long courseBoardId, Pageable pageable);
    Page<CommentsDto> findReplyByIds(Long id, Pageable pageable);
}
