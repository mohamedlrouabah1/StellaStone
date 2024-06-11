package com.stormstars.stellastone.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.stormstars.stellastone.controller.user.UserController;
import com.stormstars.stellastone.service.construction.ConstructionService;
import com.stormstars.stellastone.service.user.UserService;

@Deprecated
@Component
public class SecurityHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    @Autowired
    UserService service;

    @Autowired
    ConstructionService fservice;

    public void addContentTypeHeader(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/javascript");
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        UserController.setCurrentUser(service.findByUsername(authentication.getName()));
        addContentTypeHeader(response);
        response.sendRedirect("/home");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
    }
}
