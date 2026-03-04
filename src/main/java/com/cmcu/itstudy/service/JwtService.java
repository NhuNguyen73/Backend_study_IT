package com.cmcu.itstudy.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cmcu.itstudy.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final int ACCESS_TOKEN_EXPIRATION_MINUTES = 15;
    private static final String DEFAULT_ROLE = "USER";

    private final SecretKey signingKey;

    public JwtService(@Value("${jwt.secret}") String base64Secret) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    }

    /**
     * Generate access token for user.
     *
     * @param user the user entity
     * @return JWT access token string
     */
    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_MINUTES * 60 * 1000L);

        List<String> roles = List.of(DEFAULT_ROLE);

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extract user ID from JWT token.
     *
     * @param token the JWT token
     * @return user ID as UUID, or null if token is invalid
     */
    public UUID extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String subject = claims.getSubject();
            if (subject != null) {
                return UUID.fromString(subject);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extract email from JWT token.
     *
     * @param token the JWT token
     * @return email as String, or null if token is invalid
     */
    public String extractEmail(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("email", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Validate JWT token against user.
     *
     * @param token the JWT token
     * @param user the user entity
     * @return true if token is valid, false otherwise
     */
    public boolean isTokenValid(String token, User user) {
        try {
            Claims claims = extractAllClaims(token);
            UUID tokenUserId = UUID.fromString(claims.getSubject());
            String tokenEmail = claims.get("email", String.class);

            boolean isUserIdMatch = tokenUserId.equals(user.getId());
            boolean isEmailMatch = tokenEmail != null && tokenEmail.equals(user.getEmail());
            boolean isNotExpired = claims.getExpiration().after(new Date());

            return isUserIdMatch && isEmailMatch && isNotExpired;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get access token expiration time in seconds.
     *
     * @return expiration time in seconds
     */
    public Long getAccessTokenExpiration() {
        return (long) ACCESS_TOKEN_EXPIRATION_MINUTES * 60;
    }

    /**
     * Extract all claims from JWT token.
     *
     * @param token the JWT token
     * @return claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

