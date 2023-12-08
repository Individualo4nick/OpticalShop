package com.example.store_authorization.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.store_authorization.domain.entity.Refresh;
import com.example.store_authorization.domain.entity.User;
import com.example.store_authorization.domain.entity.roles.Role;
import com.example.store_authorization.domain.jwt.TokenRequest;
import com.example.store_authorization.domain.jwt.TokenResponse;
import com.example.store_authorization.repository.RefreshRepository;
import com.example.store_authorization.service.TokenService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Setter
public class TokenServiceImpl implements TokenService {
    private final RefreshRepository refreshRepository;
    @Value("${jwt.secret.access}")
    private String jwtAccessSecret;
    @Value("${jwt.secret.refresh}")
    private String jwtRefreshSecret;

    public TokenServiceImpl(RefreshRepository refreshRepository) {
        this.refreshRepository = refreshRepository;
    }

    @Override
    public String generateAccessToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(jwtAccessSecret);

        Instant now = Instant.now();
        Instant exp = now.plus(5, ChronoUnit.MINUTES);

        return JWT.create()
                .withIssuer("auth-service")
                .withAudience("auth-service, gateway, optical_shop")
                .withSubject(user.getLogin())
                .withClaim("role", String.valueOf(user.getRole()))
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);
    }

    @Override
    public String generateRefreshToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(jwtRefreshSecret);

        Instant now = Instant.now();
        Instant exp = now.plus(24, ChronoUnit.HOURS);

        String refreshToken = JWT.create()
                .withIssuer("auth-service")
                .withAudience("auth-service, gateway, optical_shop")
                .withSubject(user.getLogin())
                .withClaim("role", String.valueOf(user.getRole()))
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);

        Refresh refresh = new Refresh(user.getLogin(), refreshToken);
        refreshRepository.save(refresh);
        return refreshToken;
    }

    @Override
    public TokenResponse refreshTokens(TokenRequest tokenRequest) {
        User user = getUserWithLoginAndRoleByToken(tokenRequest.getRefreshToken());
        return new TokenResponse(generateAccessToken(user), generateRefreshToken(user));
    }

    @Override
    public User getUserWithLoginAndRoleByToken(String refreshToken) {
        Algorithm algorithm = Algorithm.HMAC256(jwtRefreshSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String roleUser = decodedJWT.getClaim("role").toString();
        roleUser = roleUser.substring(1, roleUser.length() - 1);
        return new User().setLogin(decodedJWT.getSubject()).setRole(Role.valueOf(roleUser));
    }
}
