package com.example.optical_shop.controller;

import com.example.optical_shop.dto.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping
    public ResponseEntity<String> register(@RequestBody LoginDto loginDto) {
        //userService.register(user);
        return ResponseEntity.ok("Registered");
    }
}
