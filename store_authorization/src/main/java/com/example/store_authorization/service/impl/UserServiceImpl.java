package com.example.store_authorization.service.impl;

import com.example.store_authorization.domain.entity.User;
import com.example.store_authorization.domain.jwt.JwtRequest;
import com.example.store_authorization.exception.LoginException;
import com.example.store_authorization.repository.UserRepository;
import com.example.store_authorization.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userRepository.save(user);
    }

    @Override
    public void checkCredentials(JwtRequest jwtRequest) throws LoginException {
        Optional<User> optionalUser = userRepository.getUserByLogin(jwtRequest.getLogin());
        if (optionalUser.isEmpty())
            throw new LoginException("Client with login: " + jwtRequest.getLogin() + " not found");

        if (!BCrypt.checkpw(jwtRequest.getPassword(), optionalUser.get().getPassword()))
            throw new LoginException("Secret is incorrect");
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.getUserByLogin(login).get();
    }
}
