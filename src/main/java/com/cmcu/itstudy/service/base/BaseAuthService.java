package com.cmcu.itstudy.service.base;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Base abstract service for authentication related helpers.
 */
public abstract class BaseAuthService extends BaseService {

    protected final PasswordEncoder passwordEncoder;

    protected BaseAuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Encode raw password using PasswordEncoder.
     */
    protected String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verify password match.
     */
    protected boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}