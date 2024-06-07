package com.example.store_authorization.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.store_authorization.domain.entity.Refresh;
import com.example.store_authorization.domain.entity.User;
import com.example.store_authorization.repository.RefreshRepository;
import com.example.store_authorization.service.AuthService;
import com.example.store_authorization.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final TokenService tokenService;
    private final RefreshRepository refreshRepository;
    @Value("${jwt.secret.refresh}")
    private String jwtRefreshSecret;

    public AuthServiceImpl(TokenService tokenService, RefreshRepository refreshRepository) {
        this.tokenService = tokenService;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public boolean checkRefreshToken(String refreshToken) {
        Algorithm algorithm = Algorithm.HMAC256(jwtRefreshSecret);
        if (refreshToken!= null && checkToken(algorithm, refreshToken)){
            User user = tokenService.getUserWithLoginAndRoleByToken(refreshToken);
            Optional<Refresh> refresh = refreshRepository.findById(user.getLogin());
            return refresh.isPresent() && refreshToken.equals(refresh.get().getRefreshToken());
        }
        return false;
    }
    public boolean checkToken(Algorithm algorithm, String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            if (!decodedJWT.getIssuer().equals("auth-service")) {
                log.error("Issuer is incorrect");
                return false;
            }
            if (!decodedJWT.getAudience().toString().contains("auth-service")) {
                log.error("Audience is incorrect");
                return false;
            }
        } catch (JWTVerificationException e) {
            log.error("Token is invalid: " + e.getMessage());
            return false;
        }
        return true;
    }
}
