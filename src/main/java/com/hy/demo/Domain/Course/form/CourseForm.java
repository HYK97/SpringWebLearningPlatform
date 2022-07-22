package com.hy.demo.Domain.Course.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
@AllArgsConstructor
public class CourseForm {

    MultipartFile thumbnail;
    @Length(min = 1, max = 400, message = "최소 1자이상  최대 400자 이하로 작성해주세요")
    String courseName;
    @Length(min = 1, max = 99999, message = "최소 1자이상  최대 99999자 이하로 작성해주세요")
    String courseExplanation;
}
