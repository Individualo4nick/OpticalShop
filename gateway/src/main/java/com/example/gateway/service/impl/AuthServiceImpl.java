package com.example.gateway.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.gateway.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Value("${jwt.secret.access}")
    private String jwtAccessSecret;
    @Override
    public boolean checkAccessToken(String accessToken) {
        Algorithm algorithm = Algorithm.HMAC256(jwtAccessSecret);
        return checkToken(algorithm, accessToken);
    }

    public boolean checkToken(Algorithm algorithm, String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            if (!decodedJWT.getIssuer().equals("auth-service")) {
                System.out.println(decodedJWT.getIssuer());
                log.error("Issuer is incorrect");
                return false;
            }
            if (!decodedJWT.getAudience().toString().contains("gateway")) {
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
