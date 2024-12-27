package com.wiser.consulting.ordering_service.dto;

import com.wiser.consulting.ordering_service.entity.OrderOwner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewOrderDto {
    private String cartUid;
    private OrderOwner owner;
    private String pickupAddress;
    private String expectedPickupDate;
    private String country;
}
