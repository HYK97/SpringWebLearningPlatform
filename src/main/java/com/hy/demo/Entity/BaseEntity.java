package com.hy.demo.Entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @UpdateTimestamp
    private Timestamp lastLoginDate;
    @CreationTimestamp
    private Timestamp createDate;
}

