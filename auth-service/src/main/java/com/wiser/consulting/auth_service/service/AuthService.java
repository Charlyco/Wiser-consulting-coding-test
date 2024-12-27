package com.wiser.consulting.auth_service.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import com.wiser.consulting.auth_service.dto.ApiResponse;
import com.wiser.consulting.auth_service.dto.AuthResponse;
import com.wiser.consulting.auth_service.dto.CreateCustomerDto;
import com.wiser.consulting.auth_service.dto.CreateEmployeeDto;

@Service
public interface AuthService {

    AuthResponse loginWithEmailAndPassword(String email, String password, HttpServletRequest request);

    ApiResponse signOut(HttpServletRequest request);

    ApiResponse createCustomer(CreateCustomerDto createCustomerDto, HttpServletRequest request);

    ApiResponse createEmployee(CreateEmployeeDto createEmployeeDto);

    AuthResponse loginEmployee(String email, String password);

    Boolean isInventoryAdmin(HttpServletRequest request);

    ApiResponse getUserByAccessToken(HttpServletRequest request);

    Boolean isCustomerService(HttpServletRequest request);

    Long getUserIdByAccessToken(HttpServletRequest request);
    
}
