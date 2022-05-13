package com.hy.demo.Config.OAuth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class LoginFailHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException accessException) throws IOException, ServletException {


        // 향후 세분화할예정
        if (accessException instanceof AuthenticationServiceException ||
                accessException instanceof BadCredentialsException ||
                accessException instanceof LockedException ||
                accessException instanceof DisabledException ||
                accessException instanceof AccountExpiredException ||
                accessException instanceof CredentialsExpiredException) {
            response.sendRedirect("/loginFailRedirect");

        }

    }

}