package com.example.store_authorization.domain.jwt;

import lombok.Getter;

@Getter
public class TokenRequest {
    private String refreshToken;
}
