package com.hy.demo.Domain.Board.form;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@ToString
public class CourseBoardForm {
    @Length(min = 1, max = 400)
    private String title;
    @Length(min = 1, max = 99999999)
    private String contents;
    private List<MultipartFile> file;
}
