package com.wiser.consulting.ordering_service.dto;

public record ApiResponse(String message, int code, boolean status, Object data) {}