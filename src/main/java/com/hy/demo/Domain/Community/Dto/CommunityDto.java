package com.hy.demo.Domain.Community.Dto;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.Community.Entity.Community;
import com.hy.demo.Domain.User.Dto.UserDto;
import com.hy.demo.Domain.User.Entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.sql.Timestamp;


@Data
@NoArgsConstructor
public class CommunityDto {

    private Long id;

    private UserDto user;

    private String title;

    private String contents;

    private Date createDate;

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
