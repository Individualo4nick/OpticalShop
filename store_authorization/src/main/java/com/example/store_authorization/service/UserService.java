package com.example.store_authorization.service;

import com.example.store_authorization.domain.entity.User;
import com.example.store_authorization.domain.jwt.JwtRequest;
import com.example.store_authorization.exception.LoginException;

public interface UserService {
    void register(User user);
    void checkCredentials(JwtRequest jwtRequest) throws LoginException;
    User getUserByLogin(String login);
}
