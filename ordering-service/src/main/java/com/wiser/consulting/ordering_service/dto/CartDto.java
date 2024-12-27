package com.wiser.consulting.ordering_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import com.wiser.consulting.ordering_service.entity.CartItem;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CartDto {
    private String cartId;
    private String ownerEmail;
    private String status;
    private String createdAt;
    private String lastUpdatedAt;
    private Set<CartItem> cartItemSet;
    private Double totalPrice;
}
