package com.wiser.consulting.ordering_service.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import com.wiser.consulting.ordering_service.dto.ApiResponse;
import com.wiser.consulting.ordering_service.dto.CartDto;
import com.wiser.consulting.ordering_service.dto.NewOrderDto;

@Service
public interface OrderService {
    ApiResponse saveOrder(NewOrderDto orderDto, HttpServletRequest request);

    ApiResponse getAlOrders(HttpServletRequest request);

    ApiResponse getSingleOrderDetail(String orderNumber, HttpServletRequest request);

    ApiResponse createCart(CartDto cartDto, HttpServletRequest request);

    ApiResponse updateCart(String cartId, CartDto cartDto, HttpServletRequest request);

    
}
