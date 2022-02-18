package com.hy.demo.Config.OAuth;

import com.hy.demo.Config.Auth.PrincipalDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class LoginFailHandler implements AuthenticationFailureHandler {




    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException accessException) throws IOException, ServletException {




        // 향후 세분화할예정
        if (accessException instanceof AuthenticationServiceException ||
                accessException instanceof BadCredentialsException ||
                accessException instanceof LockedException ||
                accessException instanceof DisabledException||
                accessException instanceof AccountExpiredException ||
                accessException instanceof CredentialsExpiredException) {
            response.sendRedirect("/loginFailRedirect");

        }

    }

}