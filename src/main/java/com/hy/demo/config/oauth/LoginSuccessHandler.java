package com.hy.demo.config.oauth;

import com.hy.demo.config.auth.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public LoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.debug("principal = {} ", principal.getAttributes());
        if (principal.isFlag()) {
            //ajax 값보내기
            if (principal.getUser().getProvider() != null) {
                response.sendRedirect("/main/index"); //Oauth 로그인
            } else {
                response.sendRedirect("/loginRedirect"); //일반 로그인
            }
        } else if (!principal.isFlag()) { //oauth 회원가입중 login -> joinForm

            response.sendRedirect("/joinForm");
        } else {
            log.error("오류 OAUTH 로그인 실패");
            response.sendRedirect("/error");
        }
    }

}
