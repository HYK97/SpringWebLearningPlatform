package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Entity.SummerNoteImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<SummerNoteImage, Long>{
}
