package com.example.gateway.controller;

import com.example.gateway.dto.LoginDto;
import com.example.gateway.dto.RegisterDto;
import com.example.gateway.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class RegisterController {
    //TODO: Change WebClient to RestClient when Spring Cloud update
    private final WebClient authClient = WebClient.create("http://localhost:8080");
    private final WebClient shopClient = WebClient.create("http://localhost:8888");

    @PostMapping("/auth/registration")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        ResponseEntity<String> responseAuth = authClient.post().uri("/auth")
                .body(Mono.just(registerDto), RegisterDto.class)
                .exchangeToMono(response -> response.toEntity(String.class))
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
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("Registered");
        return ResponseEntity.ok(responseDto);
    }
}
