package com.example.store_authorization.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("refresh")
@AllArgsConstructor
@Getter
@Setter
public class Refresh implements Serializable {
    private String id;
    private String refreshToken;
}
