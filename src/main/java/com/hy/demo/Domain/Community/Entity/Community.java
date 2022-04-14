package com.hy.demo.Domain.Community.Entity;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.Comments.Entity.Comments;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.User.Entity.User;
import lombok.*;

import javax.persistence.*;
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


}
