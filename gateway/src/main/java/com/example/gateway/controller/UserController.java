package com.example.gateway.controller;

import com.example.gateway.dto.ChangeUserInfoDto;
import com.example.gateway.dto.LoginDto;
import com.example.gateway.dto.RegisterDto;
import com.example.gateway.filter.TokenValidationFilter;
import com.example.gateway.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
        if (responseAuth.getStatusCode() == HttpStatus.OK) {
            LoginDto loginDto = new LoginDto();
            loginDto.login = registerDto.login;
            shopClient.post().uri("/user")
                    .body(Mono.just(loginDto), LoginDto.class)
                    .exchangeToMono(response -> response.toEntity(String.class))
                    .block();
        } else
            return ResponseEntity.status(responseAuth.getStatusCode()).body(responseAuth.getBody());
        return ResponseEntity.ok(responseAuth.getBody());
    }
    @PatchMapping("/user")
    public ResponseEntity<?> updateUserInfo(@RequestBody ChangeUserInfoDto changeUserInfoDto, ServerHttpRequest request) {
        String token = filter.getTokenFromRequest(request);
        if (authService.checkAccessToken(token)) {
            String login = authService.getLoginByToken(token);
            ResponseEntity<?> response = shopClient.patch().uri("/user/" + login)
                    .body(Mono.just(changeUserInfoDto), ChangeUserInfoDto.class)
                    .exchangeToMono(response1 -> response1.toEntity(Object.class))
                    .block();
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
