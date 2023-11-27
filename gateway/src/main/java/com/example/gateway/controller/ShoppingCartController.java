package com.example.gateway.controller;

import com.example.gateway.filter.TokenValidationFilter;
import com.example.gateway.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final AuthService authService;
    private final TokenValidationFilter filter;
    private final WebClient shopClient = WebClient.create("http://localhost:8888");

    public ShoppingCartController(AuthService authService, TokenValidationFilter filter) {
        this.authService = authService;
        this.filter = filter;
    }

    @GetMapping
    public ResponseEntity<?> getUserCart(ServerHttpRequest request) {
        String token = filter.getTokenFromRequest(request);
        if (authService.checkAccessToken(token)) {
            String login = authService.getLoginByToken(token);
            ResponseEntity<?> response = shopClient.get().uri("/cart/" + login)
                    .exchangeToMono(response1 -> response1.toEntityList(Object.class))
                    .block();
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @DeleteMapping("/{cart_id}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable Long cart_id, ServerHttpRequest request) {
        String token = filter.getTokenFromRequest(request);
        if (authService.checkAccessToken(token)) {
            String login = authService.getLoginByToken(token);
            ResponseEntity<?> response = shopClient.delete().uri("/cart/" + login + "/" + cart_id.toString())
                    .exchangeToMono(response1 -> response1.toEntity(Object.class))
                    .block();
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
