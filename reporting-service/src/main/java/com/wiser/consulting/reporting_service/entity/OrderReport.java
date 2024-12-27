package com.wiser.consulting.reporting_service.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderReport {
    @Id
    private String orderNumber;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ItemDto> cartItemSet;
    private OrderOwner owner;
    private String pickupAddress;
    private String orderDateTime;
    private Double totalItemCost;
    private Double shippingCost;
    private Double totalCost;
}
