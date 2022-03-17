package com.hy.demo.Domain.Board.Entity;

import com.hy.demo.Domain.BaseEntity;
import com.hy.demo.Domain.File.Entity.File;
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
public class Notice extends BaseEntity {

    @Id
    @Column(name = "Notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "User_id")
    private User user;

    @OneToMany(mappedBy = "notice",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<File> file=new ArrayList<>();

    private String Contents;

    private int view;

    public void addNotice(User user) {
        this.user=user;
        user.getNotices().add(this);
    }

}
