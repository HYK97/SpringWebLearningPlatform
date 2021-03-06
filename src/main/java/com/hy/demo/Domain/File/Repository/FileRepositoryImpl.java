package com.hy.demo.Domain.File.Repository;

import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Entity.File;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;

import java.util.List;
import java.util.Optional;

import static com.hy.demo.Domain.Board.Entity.QCourseBoard.courseBoard;
import static com.hy.demo.Domain.File.Entity.QFile.file;


public class FileRepositoryImpl extends QueryDsl4RepositorySupport implements FileRepositoryCustom {

    public FileRepositoryImpl() {
        super(File.class);
    }

    @Override
    public List<FileDto> findFileIdByCourseId(Long courseBoardId) {
        return select(Projections.constructor(FileDto.class,
                file.id,
                file.origFileName,
                file.fileSize
        ))
                .from(file)
                .where(file.courseBoard.id.eq(courseBoardId))
                .fetch();
    }

    @Override
    public Optional<File> findFetchById(Long fileId) {
        return Optional.ofNullable(select(file).
                from(file)
                .where(file.id.eq(fileId))
                .leftJoin(file.courseBoard, courseBoard)
                .fetchJoin()
                .fetchOne());
    }

    public Optional<List<File>> findByCourseBoardId(Long courseBoardId) {
        return Optional.ofNullable(select(file)
                .from(file)
                .where(file.courseBoard.id.eq(courseBoardId))
                .fetch()
        );
    }

    @Override
    public Long deleteByCourseBoardId(Long courseBoardId) {
        return getQueryFactory().delete(file).where(file.courseBoard.id.eq(courseBoardId)).execute();
    }


}
