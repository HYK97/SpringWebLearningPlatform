package com.hy.demo.config;


import com.hy.demo.config.oauth.PrincipalOauth2UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import sun.rmi.runtime.Log;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터 -> 스프링 필터체인 등록
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true) //secured 애노테이션 활성화 preAuthorize 애노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // authenticated는 인증해야한다는것 즉 로그인필요
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or  hasRole('ROLE_MANAGER')") //access는 권한이필요하다는것 (로그인포함)
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 위의 페이지 3개 이외는 아무나 접근하게 해주는 체인.
                .and() //만약에 권한이 없는 페이지로 들어갈때 로그인페이지로 가게해주는 체인.
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") //login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행해줌 즉 컨트롤러에 /login필요없음
                .defaultSuccessUrl("/") //로그인 완료됬을대 페이지
                .and()
                .oauth2Login()
                .loginPage("/loginForm") //구글로그인 완료된 후처리 필요
                //1. 코드받기(인증) 2. 엑세스토큰(권한)
                //3. 사용자 프로필 정보를 가져오고 4-1 그정보를 토대로 회원가입을 진행시키거나
                //4-2 기본정보(이메일,아이디,이름,전화번호) 정보가 모자라면 ex) 쇼핑몰 ->(집주소), 백화점몰 ->(등급)
                .userInfoEndpoint()
                .userService(principalOauth2UserService); //Tip. 코드X,(액세스 토큰 +사용자 프로필정보)
    }
}
