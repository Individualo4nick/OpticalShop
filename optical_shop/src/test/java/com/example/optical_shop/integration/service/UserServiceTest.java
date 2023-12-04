package com.example.optical_shop.integration.service;

import com.example.optical_shop.dto.ChangeUserInfoDto;
import com.example.optical_shop.dto.LoginDto;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.integration.IntegrationTestBase;
import com.example.optical_shop.repository.UserRepository;
import com.example.optical_shop.service.UserService;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends IntegrationTestBase {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUser_ValidLoginDto_UserIsSaved() {
        LoginDto loginDto = new LoginDto("test_user");

        userService.registerUser(loginDto);

        assertTrue(userRepository.findUserByLogin(loginDto.getLogin()).isPresent());
        assertEquals(loginDto.getLogin(), userRepository.findUserByLogin(loginDto.getLogin()).get().getLogin());
    }

    @Test
    void getUserByLogin_ExistingUser_ReturnsOptionalUser() {
        String login = "user1";

        Optional<User> result = userService.getUserByLogin(login);

        assertTrue(result.isPresent());
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(userRepository.findUserByLogin(login).get());
    }

    @Test
    void getUserByLogin_NonExistingUser_ReturnsEmptyOptional() {
        String login = "non_existing_user";

        Optional<User> result = userService.getUserByLogin(login);

        assertTrue(result.isEmpty());
    }

    @Test
    void changeUserInfo_ExistingUser_UserInfoIsUpdated() {
        String login = "user1";
        ChangeUserInfoDto changeUserInfoDto = new ChangeUserInfoDto("newEmail");

        userService.changeUserInfo(login, changeUserInfoDto);

        assertEquals(changeUserInfoDto.getEmail(), userRepository.findUserByLogin(login).get().getEmail());
    }
}
