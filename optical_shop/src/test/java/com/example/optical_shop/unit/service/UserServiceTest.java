package com.example.optical_shop.unit.service;

import com.example.optical_shop.dto.ChangeUserInfoDto;
import com.example.optical_shop.dto.LoginDto;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.repository.UserRepository;
import com.example.optical_shop.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_ValidLoginDto_UserIsSaved() {
        LoginDto loginDto = new LoginDto("test_user");

        userService.registerUser(loginDto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertNotNull(savedUser);
        assertEquals(loginDto.getLogin(), savedUser.getLogin());
    }

    @Test
    void getUserByLogin_ExistingUser_ReturnsOptionalUser() {
        String login = "test_user";
        User expectedUser = new User();
        expectedUser.setLogin(login);

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(expectedUser));

        Optional<User> result = userService.getUserByLogin(login);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        verify(userRepository).findUserByLogin(login);
    }

    @Test
    void getUserByLogin_NonExistingUser_ReturnsEmptyOptional() {
        String login = "non_existing_user";

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByLogin(login);

        assertFalse(result.isPresent());
        verify(userRepository).findUserByLogin(login);
    }

    @Test
    void changeUserInfo_ExistingUser_UserInfoIsUpdated() {
        String login = "test_user";
        ChangeUserInfoDto changeUserInfoDto = new ChangeUserInfoDto("newEmail");

        userService.changeUserInfo(login, changeUserInfoDto);

        verify(userRepository).changeUserInfo(login, changeUserInfoDto);
    }
}
