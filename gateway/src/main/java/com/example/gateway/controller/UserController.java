package com.example.gateway.controller;

import com.example.gateway.dto.ChangeUserInfoDto;
import com.example.gateway.dto.LoginDto;
import com.example.gateway.dto.RegisterDto;
import com.example.gateway.filter.TokenValidationFilter;
import com.example.gateway.service.AuthService;
import jakarta.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;

@RestController
public class UserController {
    //TODO: Change WebClient to RestClient when Spring Cloud update
    private final WebClient authClient = WebClient.create("http://localhost:8080");
    private final WebClient shopClient = WebClient.create("http://localhost:8888");
    private final AuthService authService;
    private final TokenValidationFilter filter;

    public UserController(AuthService authService, TokenValidationFilter filter) {
        this.authService = authService;
        this.filter = filter;
    }

    @PostMapping("/auth/registration")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        ResponseEntity<?> responseAuth = authClient.post().uri("/auth")
                .body(Mono.just(registerDto), RegisterDto.class)
                .exchangeToMono(response -> response.toEntity(Object.class))
                .block();
        if (responseAuth.getStatusCode() == HttpStatus.CREATED) {
            LoginDto loginDto = new LoginDto();
            loginDto.setLogin(registerDto.login);
            shopClient.post().uri("/user")
                    .body(Mono.just(loginDto), LoginDto.class)
                    .exchangeToMono(response -> response.toEntity(String.class))
                    .block();
        } else
            return ResponseEntity.status(responseAuth.getStatusCode()).body(responseAuth.getBody());
        return ResponseEntity.ok(responseAuth.getBody());
    }
    @PutMapping("/user")
    public ResponseEntity<?> updateUserInfo(@RequestBody ChangeUserInfoDto changeUserInfoDto, ServerHttpRequest request) {
        String token = filter.getTokenFromRequest(request);
        if (authService.checkAccessToken(token)) {
            String login = authService.getLoginByToken(token);
            ResponseEntity<?> response = shopClient.put().uri("/user/" + login)
                    .body(Mono.just(changeUserInfoDto), ChangeUserInfoDto.class)
                    .exchangeToMono(response1 -> response1.toEntity(Object.class))
                    .block();
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } else if (token != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(ServerHttpRequest request) {
        String token = filter.getTokenFromRequest(request);
        if (authService.checkAccessToken(token)) {
            String login = authService.getLoginByToken(token);
            ResponseEntity<?> response = shopClient.get().uri("/user/" + login)
                    .exchangeToMono(response1 -> response1.toEntity(Object.class))
                    .block();
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } else if (token != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/secretRegister")
    public ResponseEntity<?> getSecretRegister(){
        RegisterDto registerDto = new RegisterDto();
        registerDto.login = "admin";
        registerDto.password = "wtf123";
        ResponseEntity<?> responseAuth = authClient.post().uri("/auth/secretRegister")
                .body(Mono.just(registerDto), RegisterDto.class)
                .exchangeToMono(response -> response.toEntity(Object.class))
                .block();
        if (responseAuth.getStatusCode() == HttpStatus.CREATED) {
            LoginDto loginDto = new LoginDto();
            loginDto.setLogin(registerDto.login);
            shopClient.post().uri("/user")
                    .body(Mono.just(loginDto), LoginDto.class)
                    .exchangeToMono(response -> response.toEntity(String.class))
                    .block();
        } else
            return ResponseEntity.status(responseAuth.getStatusCode()).body(responseAuth.getBody());
        return ResponseEntity.ok(responseAuth.getBody());
    }
}
