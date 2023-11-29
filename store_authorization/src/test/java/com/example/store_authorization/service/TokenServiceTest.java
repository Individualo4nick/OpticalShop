package com.example.store_authorization.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.store_authorization.domain.entity.User;
import com.example.store_authorization.domain.entity.roles.Role;
import com.example.store_authorization.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @Mock
    private Logger mockLogger;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private final String jwtRefreshSecret = "zL1HB3Pch05Avfynovxrf/kpF9O2m4NCWKJUjEp27s9J2jEG3ifiKCGylaZ8fDeoONSTJP/wAzKawB8F9rOMNg==";
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenService = new TokenServiceImpl();
        tokenService.setJwtRefreshSecret(jwtRefreshSecret);
    }

    @Test
    void getUserWithLoginAndRoleByToken_ValidToken_ReturnsUser() {
        Algorithm algorithm = Algorithm.HMAC256(jwtRefreshSecret);
        String token = JWT.create()
                .withIssuer("auth-service")
                .withAudience("auth-service, gateway, optical_shop")
                .withSubject("Vitaliy1")
                .withClaim("role", "ADMIN")
                .sign(algorithm);

        when(mockLogger.isErrorEnabled()).thenReturn(false);

        User result = tokenService.getUserWithLoginAndRoleByToken(token);

        assertNotNull(result);
        assertEquals("Vitaliy1", result.getLogin());
        assertEquals(Role.ADMIN, result.getRole());
        verify(mockLogger, never()).error(anyString());
    }
}