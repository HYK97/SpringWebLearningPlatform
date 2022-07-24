package com.hy.demo.Domain.User.form;

import com.hy.demo.Domain.User.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@ToString
@AllArgsConstructor
public class UserUpdateForm {


    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    @Email(message = "유효한 이메일을 작성하세요", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Length(min = 1, max = 200, message = "1자 이상 200자 이하로 작성하세요 ")
    private String email;

    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    @Length(min = 1, max = 2000, message = "1자 이상 2000자 이하로 작성하세요 ")
    private String selfIntroduction;
    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    @Length(min = 1, max = 200, message = "1자 이상 200자 이하로 작성하세요 ")
    private String nickname;

    public UserDto toDto() {
        UserDto dto = new UserDto();
        dto.setEmail(this.email);
        dto.setNickname(this.nickname);
        dto.setSelfIntroduction(this.selfIntroduction);
        return dto;
    }


}
