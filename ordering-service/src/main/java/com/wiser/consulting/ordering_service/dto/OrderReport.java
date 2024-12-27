package com.wiser.consulting.ordering_service.dto;

import java.util.List;

import com.wiser.consulting.ordering_service.entity.OrderOwner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class OrderReport {
    private String orderNumber;
    private List<ItemDto> cartItemSet;
    private OrderOwner owner;
    private String pickupAddress;
    private String orderDateTime;
    private Double totalItemCost;
    private Double shippingCost;
    private Double totalCost;
}
