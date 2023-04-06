package com.hy.demo.domain.community.entity;

import com.hy.demo.domain.BaseEntity;
import com.hy.demo.domain.comments.entity.Comments;
import com.hy.demo.domain.community.dto.CommunityDto;
import com.hy.demo.domain.course.entity.Course;
import com.hy.demo.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Community extends BaseEntity {

    @Id
    @Column(name = "Community_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "User_id")
    private User user;

    private String title;
    @Column(length = 100000000)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Course_id")
    private Course course;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();

    public void addNotice(User user) {
        this.user = user;
        user.getCommunity().add(this);
    }

    public void updateCommunity(Community community) {
        this.title = community.getTitle();
        this.contents = community.getContents();
    }

    public CommunityDto changeDto() {
        CommunityDto communityDto = new CommunityDto();
        communityDto.setId(this.id);
        communityDto.setTitle(this.title);
        communityDto.setContents(this.contents);
        communityDto.setCreateDate(new Date(this.getCreateDate().getTime()));
        return communityDto;
    }


}
