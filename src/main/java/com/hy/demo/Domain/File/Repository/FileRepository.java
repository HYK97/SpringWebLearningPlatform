package com.hy.demo.Domain.File.Repository;

import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Entity.File;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File,Long> {


    List<FileDto> findFileIdByCourseId(Long courseBoardId);

}
