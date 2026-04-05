package com.cmcu.itstudy.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    private static final String REG_GITHUB = "github";
    private static final String PROVIDER_GITHUB = "GITHUB";
    private static final String PROVIDER_GOOGLE = "GOOGLE";

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
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            redirectWithError(response, "invalid_oauth");
            return;
        }

        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        String email;
        String name;
        String avatar;
        String providerId;
        String provider;

        if (REG_GITHUB.equalsIgnoreCase(registrationId)) {
            email = oAuth2User.getAttribute("email");
            String login = oAuth2User.getAttribute("login");
            name = oAuth2User.getAttribute("name");
            avatar = oAuth2User.getAttribute("avatar_url");
            Object idAttr = oAuth2User.getAttribute("id");
            providerId = idAttr != null ? String.valueOf(idAttr) : null;
            provider = PROVIDER_GITHUB;

            if (email == null || email.isBlank()) {
                if (login != null && !login.isBlank()) {
                    email = login.trim() + "@github.com";
                }
            }
            if (email == null || email.isBlank()) {
                redirectWithError(response, "missing_email");
                return;
            }
        } else {
            // Google: openid profile — giữ hành vi cũ
            email = oAuth2User.getAttribute("email");
            if (email == null || email.isBlank()) {
                redirectWithError(response, "missing_email");
                return;
            }
            name = oAuth2User.getAttribute("name");
            avatar = oAuth2User.getAttribute("picture");
            Object sub = oAuth2User.getAttribute("sub");
            providerId = sub != null ? String.valueOf(sub) : null;
            provider = PROVIDER_GOOGLE;
        }

        TokenResponseDto token = authService.loginWithOAuth(email, name, avatar, providerId, provider);

        String target = UriComponentsBuilder.fromUriString(frontendOAuth2SuccessUrl)
                .queryParam("accessToken", token.getAccessToken())
                .queryParam("refreshToken", token.getRefreshToken())
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(target);
    }

    private void redirectWithError(HttpServletResponse response, String errorCode) throws IOException {
        String target = UriComponentsBuilder.fromUriString(frontendOAuth2SuccessUrl)
                .queryParam("error", errorCode)
                .build()
                .encode()
                .toUriString();
        response.sendRedirect(target);
    }
}
