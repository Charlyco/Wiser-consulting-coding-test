package com.wiser.consulting.auth_service.dto;

public record AuthResponse(String message, boolean status, String accessToken, Object user) {

}
