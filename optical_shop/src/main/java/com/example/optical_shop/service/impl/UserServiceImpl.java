package com.example.optical_shop.service.impl;

import com.example.optical_shop.dto.LoginDto;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.repository.UserRepository;
import com.example.optical_shop.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(LoginDto loginDto) {
        User user = new User();
        user.setLogin(loginDto.login);
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }
}
