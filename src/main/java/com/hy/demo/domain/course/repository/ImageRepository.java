package com.hy.demo.domain.course.repository;

import com.hy.demo.domain.course.entity.SummerNoteImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<SummerNoteImage, Long> {
}
