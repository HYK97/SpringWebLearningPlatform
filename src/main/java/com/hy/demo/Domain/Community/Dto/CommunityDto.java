package com.hy.demo.Domain.Community.Dto;

import com.hy.demo.Domain.Community.Entity.Community;
import com.hy.demo.Domain.User.Dto.UserDto;
import com.hy.demo.Domain.User.Entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;
import java.sql.Timestamp;


@Data
@NoArgsConstructor
public class CommunityDto {

    private Long id;


    private UserDto user;
    @Length(min = 1, max = 400, message = "최소 1자이상  최대 1000자 이하로 작성해주세요")
    private String title;
    @Length(min = 1, max = 99999, message = "최소 1자이상  최대 99999자 이하로 작성해주세요")
    private String contents;

    private Date createDate;

    private Integer myCommunity;

    public CommunityDto(Long id, User user, String title, String contents, Timestamp createDate) {
        this.id = id;
        this.user = user.changeDto();
        this.title = title;
        this.contents = contents;
        this.createDate = new Date(createDate.getTime());
    }

    public Community toEntity() {
        return Community.builder()
                .id(id)
                .title(this.title)
                .contents(this.contents)
                .build();

    }

}
