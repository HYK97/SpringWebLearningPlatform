package com.hy.demo.Domain.Board.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
@ToString
@AllArgsConstructor
public class CourseBoardForm {
    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    @Length(min = 1, max = 400, message = "최소 1자이상  최대 400자 이하로 작성해주세요")
    private String title;
    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    @Length(min = 1, max = 99999, message = "최소 1자이상  최대 99999자 이하로 작성해주세요")
    private String contents;
    private List<MultipartFile> file;
}
