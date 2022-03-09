package com.hy.demo.Domain.Course.Repository;

import com.hy.demo.Domain.Course.Entity.SummerNoteImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<SummerNoteImage, Long>{
}
