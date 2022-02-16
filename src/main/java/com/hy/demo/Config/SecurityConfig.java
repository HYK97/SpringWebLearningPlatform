package com.hy.demo.Config;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Config.OAuth.LoginSuccessHandler;
import com.hy.demo.Config.OAuth.PrincipalOauth2UserService;
import com.hy.demo.Domain.User.Contoller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import org.springframework.security.web.session.HttpSessionEventPublisher;

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
                .antMatchers("/user/manager/**").access("hasRole('ROLE_ADMIN') or  hasRole('ROLE_MANAGER')") //access는 권한이필요하다는것 (로그인포함)
                .antMatchers("/user/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/**").authenticated()
                .anyRequest().permitAll() // 위의 페이지 3개 이외는 아무나 접근하게 해주는 체인.
                .and() //만약에 권한이 없는 페이지로 들어갈때 로그인페이지로 가게해주는 체인.
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") //login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행해줌 즉 컨트롤러에 /login필요없음
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                //1. 코드받기(인증) 2. 엑세스토큰(권한)
                //3. 사용자 프로필 정보를 가져오고 4-1 그정보를 토대로 회원가입을 진행시키거나
                //4-2 기본정보(이메일,아이디,이름,전화번호) 정보가 모자라면 ex) 쇼핑몰 ->(집주소), 백화점몰 ->(등급)
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
                .and()
                .successHandler(new LoginSuccessHandler("/"));



        http.sessionManagement()

                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/loginForm?dual=1")
                .sessionRegistry(sessionRegistry());



    }
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // was가 여러개 있을 때(session clustering)
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }
}
