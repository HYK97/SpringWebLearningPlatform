package com.hy.demo.Domain.File.Repository;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.File.Dto.FileDto;

import java.util.List;

public interface FileRepositoryCustom {
    List<FileDto> findFileIdByCourseId(Long courseBoardId);
}
