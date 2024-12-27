package com.wiser.consulting.inventory_service.dto;

public record ApiResponse(String message, int code, boolean status, Object data) {
}
