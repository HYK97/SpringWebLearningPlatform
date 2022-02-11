package com.hy.demo.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Notice extends BaseEntity{

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
