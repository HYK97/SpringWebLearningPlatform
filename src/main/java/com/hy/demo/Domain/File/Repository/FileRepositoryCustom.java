package com.hy.demo.Domain.File.Repository;

import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Entity.File;

import java.util.List;
import java.util.Optional;

public interface FileRepositoryCustom {
    List<FileDto> findFileIdByCourseId(Long courseBoardId);
    Optional<File> findFetchById(Long fileId);
    Optional<List<File>> findByCourseBoardId(Long courseBoardId);
    Long deleteByCourseBoardId(Long courseBoardId);
}
