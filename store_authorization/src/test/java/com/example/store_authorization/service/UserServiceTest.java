package com.example.store_authorization.service;

import com.example.store_authorization.domain.entity.User;
import com.example.store_authorization.domain.jwt.JwtRequest;
import com.example.store_authorization.exception.LoginException;
import com.example.store_authorization.repository.UserRepository;
import com.example.store_authorization.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

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
    void register_ValidUser_PasswordIsHashedAndUserIsSaved() {
        User user = new User();
        user.setLogin("test_user");
        user.setPassword("test_password");

        userService.register(user);

        assertTrue(BCrypt.checkpw("test_password", user.getPassword()));  // Проверяем, что пароль был хеширован
        verify(userRepository).save(user);  // Проверяем, что вызвался метод save в userRepository
    }

    @Test
    void checkCredentials_ValidCredentials_NoExceptionThrown() throws LoginException {
        User user = new User();
        user.setLogin("test_user");
        user.setPassword(BCrypt.hashpw("test_password", BCrypt.gensalt()));

        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setLogin("test_user");
        jwtRequest.setPassword("test_password");

        when(userRepository.getUserByLogin(jwtRequest.getLogin())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.checkCredentials(jwtRequest));  // Проверяем, что метод не выбрасывает исключение
        verify(userRepository).getUserByLogin(jwtRequest.getLogin());  // Проверяем, что вызвался метод getUserByLogin в userRepository
    }

    @Test
    void checkCredentials_UserNotFound_ThrowsLoginException() {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setLogin("test_user");
        jwtRequest.setPassword("test_password");

        when(userRepository.getUserByLogin(jwtRequest.getLogin())).thenReturn(Optional.empty());

        assertThrows(LoginException.class, () -> userService.checkCredentials(jwtRequest));  // Проверяем, что метод выбрасывает исключение LoginException
        verify(userRepository).getUserByLogin(jwtRequest.getLogin());  // Проверяем, что вызвался метод getUserByLogin в userRepository
    }

    @Test
    void checkCredentials_IncorrectPassword_ThrowsLoginException() {
        User user = new User();
        user.setLogin("test_user");
        user.setPassword(BCrypt.hashpw("test_password", BCrypt.gensalt()));

        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setLogin("test_user");
        jwtRequest.setPassword("incorrect_password");

        when(userRepository.getUserByLogin(jwtRequest.getLogin())).thenReturn(Optional.of(user));

        assertThrows(LoginException.class, () -> userService.checkCredentials(jwtRequest));  // Проверяем, что метод выбрасывает исключение LoginException
        verify(userRepository).getUserByLogin(jwtRequest.getLogin());  // Проверяем, что вызвался метод getUserByLogin в userRepository
    }

    @Test
    void getUserByLogin_ValidLogin_ReturnsUser() {
        User expectedUser = new User();
        expectedUser.setLogin("test_user");

        when(userRepository.getUserByLogin("test_user")).thenReturn(Optional.of(expectedUser));

        User result = userService.getUserByLogin("test_user");

        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(userRepository).getUserByLogin("test_user");  // Проверяем, что вызвался метод getUserByLogin в userRepository
    }

    @Test
    void getUserByLogin_InvalidLogin_ThrowsException() {
        when(userRepository.getUserByLogin("invalid_login")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> userService.getUserByLogin("invalid_login"));  // Проверяем, что метод выбрасывает исключение
        verify(userRepository).getUserByLogin("invalid_login");  // Проверяем, что вызвался метод getUserByLogin в userRepository
    }
}
