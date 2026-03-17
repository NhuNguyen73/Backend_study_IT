package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.service.contract.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private final javax.crypto.SecretKey signingKey;
    private final int accessTokenExpirySeconds;
    private final int refreshTokenExpirySeconds;

    public JwtServiceImpl(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-seconds:900}") int accessTokenExpirySeconds,
            @Value("${jwt.refresh-token-expiration-seconds:604800}") int refreshTokenExpirySeconds
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
    }

    @Override
    public String generateAccessToken(User user, List<String> roles, List<String> permissions) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("permissions", permissions);
        return buildToken(user.getEmail(), claims, accessTokenExpirySeconds);
    }

    @Override
    public String generateRefreshToken(User user) {
        return buildToken(user.getEmail(), new HashMap<>(), refreshTokenExpirySeconds);
    }

    @Override
    public int getAccessTokenExpirySeconds() {
        return accessTokenExpirySeconds;
    }

    @Override
    public int getRefreshTokenExpirySeconds() {
        return refreshTokenExpirySeconds;
    }

    @Override
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public boolean isTokenValid(String token, User user) {
        String username = extractUsername(token);
        return username != null && username.equals(user.getEmail()) && !isTokenExpired(token);
    }

    private String buildToken(String subject, Map<String, Object> claims, int expirySeconds) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expirySeconds);
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(signingKey)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

