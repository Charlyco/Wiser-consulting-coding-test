package com.wiser.consulting.edge_service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
                    "/auth-service/api/v1/**",
                    "/auth-service/api/v1/auth/customer/signup",
                    "/auth-service/api/v1/auth/customer/login",
                    "/auth-service/api/v1/auth/employee/signup",
                    "/auth-service/api/v1/auth/employee/login",
                    "/auth-service/api/v1/auth/accessToken",
                    "/eureka"
                    );

    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}
