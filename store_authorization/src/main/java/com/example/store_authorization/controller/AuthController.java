package com.example.store_authorization.controller;

import com.example.store_authorization.domain.entity.User;
import com.example.store_authorization.domain.jwt.ErrorResponse;
import com.example.store_authorization.domain.jwt.JwtRequest;
import com.example.store_authorization.domain.jwt.TokenRequest;
import com.example.store_authorization.domain.jwt.TokenResponse;
import com.example.store_authorization.dto.ResponseDto;
import com.example.store_authorization.exception.LoginException;
import com.example.store_authorization.exception.TokenException;
import com.example.store_authorization.service.AuthService;
import com.example.store_authorization.service.TokenService;
import com.example.store_authorization.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
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
    public ResponseEntity<ResponseDto> register(@RequestBody User user) {
        userService.register(user);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("Registered");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getToken(@RequestBody JwtRequest jwtRequest) throws LoginException {
        userService.checkCredentials(jwtRequest);
        User user = userService.getUserByLogin(jwtRequest.getLogin());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "refresh=" + tokenService.generateRefreshToken(user) + "; Path=/auth/refresh; Max-Age=3600; HttpOnly");
        return ResponseEntity.ok().headers(headers).body(new TokenResponse(tokenService.generateAccessToken(user), tokenService.generateRefreshToken(user)));
    }
    @GetMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) throws TokenException {
        var cookies = request.getCookies();
        var refreshToken = cookies != null ? getRefresh(cookies) : null;
        if(authService.checkRefreshToken(refreshToken)) {
            var response = tokenService.refreshTokens(refreshToken);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", "refresh=" + response.getRefreshToken() + "; Path=/auth/refresh; Max-Age=3600; HttpOnly");
            return ResponseEntity.ok().headers(headers).body(response);
        }
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
    private String getRefresh(Cookie[] cookies){
        for (Cookie cookie : cookies){
            if (cookie.getName().equals("refresh")){
                return cookie.getValue();
            }
        }
        return null;
    }
}
