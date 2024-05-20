package com.example.store_authorization.service;

import com.example.store_authorization.domain.entity.User;
import com.example.store_authorization.domain.jwt.TokenRequest;
import com.example.store_authorization.domain.jwt.TokenResponse;

public interface TokenService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    TokenResponse refreshTokens(String tokenRequest);
    User getUserWithLoginAndRoleByToken(String refreshToken);
}
