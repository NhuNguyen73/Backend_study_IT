package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.entity.User;

import java.util.List;

public interface JwtService {

    String generateAccessToken(User user, List<String> roles, List<String> permissions);

    String generateRefreshToken(User user);

    int getAccessTokenExpirySeconds();

    int getRefreshTokenExpirySeconds();

    String extractUsername(String token);

    boolean isTokenValid(String token, User user);
}

