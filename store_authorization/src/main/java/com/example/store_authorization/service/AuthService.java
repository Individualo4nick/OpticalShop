package com.example.store_authorization.service;

public interface AuthService {
    boolean checkAccessToken(String accessToken);
    boolean checkRefreshToken(String refreshToken);
}
