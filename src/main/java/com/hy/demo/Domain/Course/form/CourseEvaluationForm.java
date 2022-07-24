package com.hy.demo.Domain.Course.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Data
@ToString
@AllArgsConstructor
public class CourseEvaluationForm {


    @Length(min = 1, max = 2000, message = "최소 1자이상  최대 2000자 이하로 작성해주세요")
    private String content;

    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5.0")
    private Double star;

    private String courseId;

}
