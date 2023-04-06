package com.hy.demo.config.auth;


import com.hy.demo.domain.user.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


@Data
@EqualsAndHashCode(of = {"userId"})
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Long userId;
    private User user;

    private Map<String, Object> attributes;

    private boolean flag;


    public PrincipalDetails(User userEntity, boolean flag) {
        this.user = userEntity;
        this.flag = flag;
        this.userId = userEntity.getId();
    }

    public PrincipalDetails(User user, Map<String, Object> attributes, boolean flag) {
        this.user = user;
        this.attributes = attributes;
        this.flag = flag;
        this.userId = user.getId();
    }

    public boolean isFlag() {
        return flag;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    //해당 User의 권한을 리턴해주는것
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //휴면 계정으로 하기로했다면 ?
        // 현재시간 -로그인 시간 -> 1년초과하면 false가 되도록 하는듯이 만들면됌
        // user.getLoginDate()
        return true;
    }


}
