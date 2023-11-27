package com.example.optical_shop.service;

import com.example.optical_shop.dto.ChangeUserInfoDto;
import com.example.optical_shop.dto.LoginDto;
import com.example.optical_shop.entity.User;

import java.util.Optional;


public interface UserService {
    void registerUser(LoginDto loginDto);
    Optional<User> getUserByLogin(String login);
    void changeUserInfo(String login, ChangeUserInfoDto changeUserInfoDto);
}
