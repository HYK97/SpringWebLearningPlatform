package com.hy.demo.Domain.File.Repository;

import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Course.Entity.CourseEvaluation;
import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Entity.File;
import com.hy.demo.Domain.File.Entity.QFile;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.types.Projections;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.hy.demo.Domain.Board.Entity.QCourseBoard.courseBoard;
import static com.hy.demo.Domain.Course.Entity.QCourse.course;
import static com.hy.demo.Domain.File.Entity.QFile.file;


public class FileRepositoryImpl extends QueryDsl4RepositorySupport implements FileRepositoryCustom {



    public FileRepositoryImpl() {
        super(File.class);
    }


    @Autowired
    Logger logger;


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
}
