package com.example.gateway.filter;

import com.example.gateway.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenValidationFilter extends
        AbstractGatewayFilterFactory<TokenValidationFilter.Config> {

    private final AuthService authService;

    public TokenValidationFilter(AuthService authService) {
        super(Config.class);
        this.authService = authService;
    }
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${auth.enabled}")
    private boolean enabled;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if(!enabled)
                return chain.filter(exchange);
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().get(AUTHORIZATION_HEADER).get(0);
            ServerHttpResponse response = exchange.getResponse();
            if (authHeader == null || authHeader.isBlank()){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            else if (!checkAuthorization(authHeader)) {
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }
            return chain.filter(exchange);
        };
    }

    public String getTokenFromRequest(ServerHttpRequest request){
        String authHeader = request.getHeaders().get(AUTHORIZATION_HEADER).get(0);

        return authHeader.substring(TOKEN_PREFIX.length());
    }

    private boolean checkAuthorization(String auth) {
        if (!auth.startsWith(TOKEN_PREFIX))
            return false;

        String token = auth.substring(TOKEN_PREFIX.length());
        return authService.checkAccessToken(token);
    }

    public static class Config {
    }
}