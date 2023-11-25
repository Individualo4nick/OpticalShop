package com.example.optical_shop.controller;

import com.example.optical_shop.dto.LoginDto;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.mapper.UserMapper;
import com.example.optical_shop.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody LoginDto loginDto) {
        userService.registerUser(loginDto);
        return ResponseEntity.ok("Registered");
    }
    @GetMapping("/{login}")
    public ResponseEntity<?> getUserByLogin(@PathVariable String login) {
        Optional<User> user = userService.getUserByLogin(login);
        if(user.isEmpty())
            return ResponseEntity.ok("User with login " + login + " not found");
        return ResponseEntity.ok(userMapper.userToUserDto(user.get()));
    }
}
