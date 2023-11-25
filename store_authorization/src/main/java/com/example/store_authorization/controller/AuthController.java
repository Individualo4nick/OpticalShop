package com.example.store_authorization.controller;

import com.example.store_authorization.domain.entity.User;
import com.example.store_authorization.domain.jwt.ErrorResponse;
import com.example.store_authorization.domain.jwt.JwtRequest;
import com.example.store_authorization.domain.jwt.TokenRequest;
import com.example.store_authorization.domain.jwt.TokenResponse;
import com.example.store_authorization.exception.LoginException;
import com.example.store_authorization.exception.TokenException;
import com.example.store_authorization.service.AuthService;
import com.example.store_authorization.service.TokenService;
import com.example.store_authorization.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthService authService;

    public AuthController(UserService userService, TokenService tokenService, AuthService authService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/token")
    public TokenResponse getToken(@RequestBody JwtRequest jwtRequest) throws LoginException {
        userService.checkCredentials(jwtRequest);
        User user = userService.getUserByLogin(jwtRequest.getLogin());
        return new TokenResponse(tokenService.generateAccessToken(user),
                tokenService.generateRefreshToken(user));
    }
    @PostMapping("/refresh")
    public TokenResponse getToken(@RequestBody TokenRequest tokenRequest) throws TokenException {
        if(authService.checkRefreshToken(tokenRequest.getRefreshToken()))
            return tokenService.refreshTokens(tokenRequest);
        else
            throw new TokenException("Not valid refresh token");
    }

    @ExceptionHandler({LoginException.class, TokenException.class})
    public ResponseEntity<ErrorResponse> handleUserRegistrationException(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> handleUserRegistrationExceptionForData(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("User with this login already exists"));
    }

}
