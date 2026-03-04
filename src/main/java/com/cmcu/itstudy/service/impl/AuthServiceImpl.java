package com.cmcu.itstudy.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcu.itstudy.dto.request.LoginRequest;
import com.cmcu.itstudy.dto.request.RegisterRequest;
import com.cmcu.itstudy.dto.response.LoginResponse;
import com.cmcu.itstudy.dto.response.RegisterResponse;
import com.cmcu.itstudy.entity.PasswordReset;
import com.cmcu.itstudy.entity.RefreshToken;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.enums.TokenType;
import com.cmcu.itstudy.enums.UserStatus;
import com.cmcu.itstudy.exception.AppException;
import com.cmcu.itstudy.repository.PasswordResetRepository;
import com.cmcu.itstudy.repository.RefreshTokenRepository;
import com.cmcu.itstudy.repository.UserRepository;
import com.cmcu.itstudy.security.UserDetailsImpl;
import com.cmcu.itstudy.service.JwtService;
import com.cmcu.itstudy.service.base.BaseAuthService;
import com.cmcu.itstudy.service.contract.AuthService;

@Service
public class AuthServiceImpl extends BaseAuthService implements AuthService {

    private static final int VERIFY_EMAIL_TOKEN_EXPIRY_HOURS = 24;
    private static final int REFRESH_TOKEN_EXPIRY_DAYS = 7;

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            PasswordResetRepository passwordResetRepository,
            RefreshTokenRepository refreshTokenRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        super(passwordEncoder);
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // ================= REGISTER =================
    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        String email = normalizeEmail(request.getEmail());

        validateEmailNotExists(email);
        validatePassword(request.getPassword());

        User user = createUser(email, request.getPassword(), request.getFullName());
        user = userRepository.save(user); // ⚠️ đảm bảo entity managed

        PasswordReset token = createVerifyEmailToken(user);
        passwordResetRepository.save(token);

        return buildRegisterResponse(user);
    }

    private void validateEmailNotExists(String email) {
        if (userRepository.existsByEmailAndNotDeleted(email)) {
            throw new AppException("Email already exists");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new AppException("Password must be at least 8 characters");
        }
    }

    private String normalizeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new AppException("Email is required");
        }
        return email.trim().toLowerCase();
    }

    private User createUser(String email, String rawPassword, String fullName) {
        return User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password(encodePassword(rawPassword))
                .fullName(fullName)
                .emailVerified(false)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private PasswordReset createVerifyEmailToken(User user) {
        LocalDateTime now = LocalDateTime.now();

        return PasswordReset.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .type(TokenType.VERIFY_EMAIL)
                .used(false)
                .expiresAt(now.plusHours(VERIFY_EMAIL_TOKEN_EXPIRY_HOURS))
                .createdAt(now)
                .build();
    }

    private RegisterResponse buildRegisterResponse(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                "Registration successful. Please verify your email."
        );
    }

    // ================= LOGIN =================
    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {

        String email = normalizeEmail(request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        updateLastLoginAt(user);
        userRepository.save(user); // ⚠️ đảm bảo update được persist

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = createAndSaveRefreshToken(user);

        return buildLoginResponse(accessToken, refreshToken.getToken());
    }

    private void updateLastLoginAt(User user) {
        user.setLastLoginAt(LocalDateTime.now());
    }

    private RefreshToken createAndSaveRefreshToken(User user) {
        LocalDateTime now = LocalDateTime.now();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .revoked(false)
                .expiresAt(now.plusDays(REFRESH_TOKEN_EXPIRY_DAYS))
                .createdAt(now)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private LoginResponse buildLoginResponse(String accessToken, String refreshToken) {
        return new LoginResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.getAccessTokenExpiration()
        );
    }
}