package com.example.gateway.service;

import com.auth0.jwt.algorithms.Algorithm;

public interface AuthService {
    boolean checkAccessToken(String accessToken);
    boolean checkRightsByToken(String accessToken, String role);
    String getLoginByToken(String accessToken);
}
