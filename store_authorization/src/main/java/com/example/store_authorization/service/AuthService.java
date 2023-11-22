package com.example.store_authorization.service;

public interface AuthService {
    boolean checkRefreshToken(String refreshToken);
}
