package com.hy.demo.config.auth;


import com.hy.demo.Entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 시큐리티가 로그인 (/login) 주소요청이 오면 낚아서 로그인 진행
 * 로그인 진행되면 session을 만들어줌 -> (Securiy ContextHolder)에 세션을 저장함
 * 이 context 홀더에 들어가는 오브젝트는 Authentication 타입의 객체를 넣어줘야함
 * 또한 Authentication 안에 user 정보가있어야된다
 * 즉 user 오브젝트를 userDetails 타입 객체로 변환해야함
 *
 * 패키징 순서는 security Session -> Authentication -> userDetails(PrincipalDetails) 순으로 접근한다.
 * */
@Data

public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;// 콤포지션

    private Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user,Map<String, Object> attributes) {
        this.user = user;
        this.attributes =attributes;
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
        Collection<GrantedAuthority> collection =new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
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
