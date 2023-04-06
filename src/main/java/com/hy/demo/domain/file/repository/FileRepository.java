package com.hy.demo.domain.file.repository;

import com.hy.demo.domain.file.dto.FileDto;
import com.hy.demo.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {


    List<FileDto> findFileIdByCourseId(Long courseBoardId);

    Optional<File> findFetchById(Long fileId);

    Optional<List<File>> findByCourseBoardId(Long courseBoardId);

    Long deleteByCourseBoardId(Long courseBoardId);
}
