package com.wiser.consulting.ordering_service.dto;

import com.wiser.consulting.ordering_service.entity.OrderOwner;
import com.wiser.consulting.ordering_service.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderDto {
    private String orderNumber;
    private CartDto cartDto;
    private OrderStatus status;
    private OrderOwner owner;
    private String pickupAddress;
    private String orderDateTime;
    private String expectedPickupDate;
    private String actualPickupDateTime;
    private Double shippingCost;
    private Double totalCost;
}
