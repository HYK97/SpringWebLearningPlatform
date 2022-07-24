package com.hy.demo.Domain.User.form;


import com.hy.demo.Domain.User.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@ToString
public class UserJoinForm {


    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    @Length(min = 1, max = 200, message = "최소 1자이상  최대 200자 이하로 작성해주세요")
    private String username;
    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    @Length(min = 1, max = 200, message = "최소 1자이상  최대 200자 이하로 작성해주세요")
    private String password;
    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    @Length(min = 1, max = 200, message = "최소 1자이상  최대 200자 이하로 작성해주세요")
    @Email(message = "유효한 이메일을 작성하세요", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    private String role; //ROLE_USER, ROLE_ADMIN
    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    @Length(min = 1, max = 2000, message = "최소 1자이상  최대 2000wk 이하로 작성해주세요")
    private String selfIntroduction;
    @NotBlank(message = "이칸은 비워둘 수 없었습니다.")
    @Length(min = 1, max = 200, message = "최소 1자이상  최대 200자 이하로 작성해주세요")
    private String nickname;

    public UserDto toDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername(this.username);
        userDto.setSelfIntroduction(this.selfIntroduction);
        userDto.setNickname(this.nickname);
        userDto.setEmail(this.email);
        userDto.setRole(this.role);
        userDto.setPassword(this.password);
        return userDto;
    }

}