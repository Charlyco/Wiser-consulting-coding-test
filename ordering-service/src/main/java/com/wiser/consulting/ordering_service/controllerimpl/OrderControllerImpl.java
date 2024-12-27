package com.wiser.consulting.ordering_service.controllerimpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.wiser.consulting.ordering_service.controller.OrderController;
import com.wiser.consulting.ordering_service.dto.ApiResponse;
import com.wiser.consulting.ordering_service.dto.CartDto;
import com.wiser.consulting.ordering_service.dto.NewOrderDto;
import com.wiser.consulting.ordering_service.service.OrderService;

@Controller
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {
    private final OrderService orderService;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<ApiResponse> createOrder(NewOrderDto orderDto) {
        return ResponseEntity.ok(orderService.saveOrder(orderDto, request));
    }

    @Override
    public ResponseEntity<ApiResponse> getSingleOrderDetail(String orderNumber) {
        return ResponseEntity.ok(orderService.getSingleOrderDetail(orderNumber, request));
    }

    @Override
    public ResponseEntity<ApiResponse> createCart(CartDto cartDto) {
        return ResponseEntity.ok(orderService.createCart(cartDto, request));
    }

    @Override
    public ResponseEntity<ApiResponse> updateCart(String cartId, CartDto cartDto) {
        return ResponseEntity.ok(orderService.updateCart(cartId, cartDto, request));
    }
}
