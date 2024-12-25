package com.wiser.consulting.auth_service.controllerimpl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.wiser.consulting.auth_service.controller.AuthController;
import com.wiser.consulting.auth_service.dto.ApiResponse;
import com.wiser.consulting.auth_service.dto.AuthResponse;
import com.wiser.consulting.auth_service.dto.CreateCustomerDto;
import com.wiser.consulting.auth_service.dto.CreateEmployeeDto;
import com.wiser.consulting.auth_service.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController{
    private final AuthService authService;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<ApiResponse> createCustomer(CreateCustomerDto createCustomerDto) {
        return ResponseEntity.ok(authService.createCustomer(createCustomerDto, request));
    }

    @Override
    public ResponseEntity<AuthResponse> loginCustomer(String email, String password) {
        return ResponseEntity.ok(authService.loginWithEmailAndPassword(email, password, request));
    }

    @Override
    public ResponseEntity<ApiResponse> signOut() {
        return ResponseEntity.ok(authService.signOut(request));
    }

    @Override
    public ResponseEntity<ApiResponse> createEmployee(CreateEmployeeDto createEmployeeDto) {
        return ResponseEntity.ok(authService.createEmployee(createEmployeeDto, request));
    }

    @Override
    public ResponseEntity<AuthResponse> loginEmployee(String email, String password) {
        return ResponseEntity.ok(authService.loginEmployee(email, password));
    }

    @Override
    public ResponseEntity<ApiResponse> getUserByAccessToken() {
        return ResponseEntity.ok(authService.getUserByAccessToken(request));
    }

}
