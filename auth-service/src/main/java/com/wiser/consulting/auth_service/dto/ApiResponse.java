package com.wiser.consulting.auth_service.dto;

public record ApiResponse(String message, Integer code, boolean status, Object data) {
}
