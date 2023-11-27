package com.example.optical_shop.controller;

import com.example.optical_shop.dto.ChangeUserInfoDto;
import com.example.optical_shop.dto.LoginDto;
import com.example.optical_shop.dto.ResponseDto;
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
    public ResponseEntity<ResponseDto> register(@RequestBody LoginDto loginDto) {
        userService.registerUser(loginDto);
        return ResponseEntity.ok(Responser.getResponse("Registered"));
    }
    @GetMapping("/{login}")
    public ResponseEntity<?> getUserByLogin(@PathVariable String login) {
        Optional<User> user = userService.getUserByLogin(login);
        if(user.isEmpty())
            return ResponseEntity.status(400).body(Responser.getResponse("User with login " + login + " not found"));
        return ResponseEntity.ok(userMapper.userToUserDto(user.get()));
    }
    @PatchMapping("/{login}")
    public ResponseEntity<?> patchUserByLogin(@PathVariable String login, @RequestBody ChangeUserInfoDto changeUserInfoDto) {
        Optional<User> user = userService.getUserByLogin(login);
        if(user.isEmpty())
            return ResponseEntity.status(400).body(Responser.getResponse("User with login " + login + " not found"));
        userService.changeUserInfo(login, changeUserInfoDto);
        return ResponseEntity.ok(Responser.getResponse("Changed"));
    }
}
