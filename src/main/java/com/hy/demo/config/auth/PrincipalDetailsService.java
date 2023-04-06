package com.hy.demo.config.auth;

import com.hy.demo.domain.user.entity.User;
import com.hy.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {


    private final UserRepository userRepository;


    @Override // 로그인 html 의 name ="username"과 아래의 매개변수 username의 이름과 같아야 동작함 만약에 파라미터 바꾸고 싶으면 SecurityConifg에서
    //파라미터 변경 추가해줘야댐
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            //로그인성공
            log.debug("userEntity.toString() = {}", userEntity.toString());
            return new PrincipalDetails(userEntity, true); // Security session(내부 Authentication(내부 UserDetails));
        }
        throw new UsernameNotFoundException(username);

    }

}
