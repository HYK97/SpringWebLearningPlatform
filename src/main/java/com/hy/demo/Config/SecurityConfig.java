package com.hy.demo.Config;


import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Config.OAuth.LoginFailHandler;
import com.hy.demo.Config.OAuth.LoginSuccessHandler;
import com.hy.demo.Config.OAuth.PrincipalOauth2UserService;
import com.hy.demo.Domain.User.Contoller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Service;


@Configuration
@EnableWebSecurity //스프링 시큐리티 필터 -> 스프링 필터체인 등록
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true) //secured 애노테이션 활성화 preAuthorize 애노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/manager/**").access("hasRole('ROLE_ADMIN') or  hasRole('ROLE_MANAGER')") //access는 권한이필요하다는것 (로그인포함)
                .antMatchers("/user/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/**").authenticated()
                .antMatchers("/main/**").authenticated()
                .antMatchers("/course/**").authenticated()
                .anyRequest().permitAll() // 위의 페이지 3개 이외는 아무나 접근하게 해주는 체인.
                .and() //만약에 권한이 없는 페이지로 들어갈때 로그인페이지로 가게해주는 체인.
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .successHandler(new LoginSuccessHandler("/main/index"))//login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행해줌 즉 컨트롤러에 /login필요없음
                .failureHandler(new LoginFailHandler())
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
                .and()
                .successHandler(new LoginSuccessHandler("/main/index"))
                .failureHandler(new LoginFailHandler());



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
