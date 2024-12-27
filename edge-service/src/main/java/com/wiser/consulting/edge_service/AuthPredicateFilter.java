package com.wiser.consulting.edge_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class AuthPredicateFilter extends AbstractGatewayFilterFactory<AuthPredicateFilter.Config> {
    private final RouteValidator routeValidator;
    private final JwtService jwtService;

    public AuthPredicateFilter(RouteValidator routeValidator, JwtService jwtService) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                log.info(exchange.getRequest().getHeaders().toString());
                if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                    throw new RuntimeException("Authorization header not found");
                }
                String authToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authToken != null && authToken.startsWith("Bearer ")) {
                    authToken = authToken.substring(7);
                }
                Objects.requireNonNull(authToken, "Authentication token is missing");

                if (jwtService.isTokenValid(authToken)) {
                    return chain.filter(exchange);
                }
                throw new RuntimeException("Invalid authentication token");
            }
            return chain.filter(exchange); // Proceed without auth checks for open endpoints
        });
    }

    @Component
    public static class Config{}
}
