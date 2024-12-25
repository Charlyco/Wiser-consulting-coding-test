package com.wiser.consulting.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wiser.consulting.auth_service.dto.ApiResponse;
import com.wiser.consulting.auth_service.dto.AuthResponse;
import com.wiser.consulting.auth_service.dto.CreateCustomerDto;
import com.wiser.consulting.auth_service.dto.CreateEmployeeDto;

@RestController
@RequestMapping("auth-service/api/v1/auth/")
public interface AuthController {
    @PostMapping("customer/signup")
    ResponseEntity<ApiResponse> createCustomer(@RequestBody CreateCustomerDto createCustomerDto);
    @PostMapping("customer/login")
    ResponseEntity<AuthResponse> loginCustomer(@RequestParam("email") String email, @RequestParam("password") String password);
    @PostMapping("logout")
    ResponseEntity<ApiResponse> signOut();
    @PostMapping("employee/signup")
    ResponseEntity<ApiResponse> createEmployee(@RequestBody CreateEmployeeDto createEmployeeDto);
    @PostMapping("employee/login")
    ResponseEntity<AuthResponse> loginEmployee(@RequestParam("email") String email, @RequestParam("password") String password);
    @GetMapping("accessToken")
    ResponseEntity<ApiResponse> getUserByAccessToken();
    
}
