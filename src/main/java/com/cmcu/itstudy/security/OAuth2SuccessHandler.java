package com.cmcu.itstudy.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.cmcu.itstudy.dto.auth.TokenResponseDto;
import com.cmcu.itstudy.service.contract.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final String frontendOAuth2SuccessUrl;

    public OAuth2SuccessHandler(
            @Lazy AuthService authService,
            @Value("${app.oauth2.frontend-success-url:http://localhost:5173/oauth2-success}") String frontendOAuth2SuccessUrl
    ) {
        this.authService = authService;
        this.frontendOAuth2SuccessUrl = frontendOAuth2SuccessUrl;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            String target = UriComponentsBuilder.fromUriString(frontendOAuth2SuccessUrl)
                    .queryParam("error", "missing_email")
                    .build()
                    .encode()
                    .toUriString();
            response.sendRedirect(target);
            return;
        }

        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        TokenResponseDto token = authService.loginWithOAuth(email, name, picture);

        String target = UriComponentsBuilder.fromUriString(frontendOAuth2SuccessUrl)
                .queryParam("accessToken", token.getAccessToken())
                .queryParam("refreshToken", token.getRefreshToken())
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(target);
    }
}
