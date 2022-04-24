package com.hy.demo.Config.Auth;

import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class PrincipalDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserRepository userRepository;


    @Override // 로그인 html 의 name ="username"과 아래의 매개변수 username의 이름과 같아야 동작함 만약에 파라미터 바꾸고 싶으면 SecurityConifg에서
    //파라미터 변경 추가해줘야댐
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        logger.info("username = " + username);
        User userEntity = userRepository.findByUsername(username);


        if (userEntity != null) {
            //로그인성공
            logger.info("userEntity.toString() = " + userEntity.toString());
            return new PrincipalDetails(userEntity, true); // Security session(내부 Authentication(내부 UserDetails));
        }
        throw new UsernameNotFoundException(username);

    }

}
