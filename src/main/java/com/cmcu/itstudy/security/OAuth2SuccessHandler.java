package com.cmcu.itstudy.security;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cmcu.itstudy.dto.auth.TokenResponseDto; // 🔥 THÊM DÒNG NÀY
import com.cmcu.itstudy.service.contract.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    public OAuth2SuccessHandler(@Lazy AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // lấy email từ Google
        String email = oAuth2User.getAttribute("email");

        // gọi logic của bạn (giống login thường)
        TokenResponseDto token = authService.loginWithOAuth(email);

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), token);
    }
}