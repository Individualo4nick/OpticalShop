package com.example.gateway.controller;

import com.example.gateway.dto.LoginDto;
import com.example.gateway.dto.RegisterDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class RegisterController {
    private final WebClient authClient = WebClient.create("http://localhost:8080");
    private final WebClient shopClient = WebClient.create("http://localhost:8888");

    @PostMapping("/auth/registration")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        authClient.post().uri("/auth")
                .body(Mono.just(registerDto), RegisterDto.class)
                .retrieve().toEntity(String.class).subscribe();

        LoginDto loginDto = new LoginDto();
        loginDto.login = registerDto.login;

        shopClient.post().uri("/user")
                .body(Mono.just(loginDto), LoginDto.class)
                .retrieve().toEntity(String.class).subscribe();
        return ResponseEntity.ok("Registered");
    }
}
