package com.wiser.consulting.ordering_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wiser.consulting.ordering_service.dto.ApiResponse;
import com.wiser.consulting.ordering_service.dto.CartDto;
import com.wiser.consulting.ordering_service.dto.NewOrderDto;

@RestController
@RequestMapping("order-service/api/v1/")
public interface OrderController {
    @PostMapping("order")
    ResponseEntity<ApiResponse> createOrder(@RequestBody NewOrderDto orderDto);
    @GetMapping("order/{orderNumber}")
    ResponseEntity<ApiResponse> getSingleOrderDetail(@PathVariable("orderNumber") String orderNumber);
    @PostMapping("cart")
    ResponseEntity<ApiResponse> createCart(@RequestBody CartDto cartDto);
    @PutMapping("cart/{cartId}")
    ResponseEntity<ApiResponse> updateCart(@PathVariable("cartId") String cartId, @RequestBody CartDto cartDto);
    
}
