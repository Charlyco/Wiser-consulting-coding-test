package com.wiser.consulting.ordering_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.wiser.consulting.ordering_service.enums.OrderStatus;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String orderNumber;
    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    private OrderStatus status;
    private OrderOwner owner;
    private String pickupAddress;
    private LocalDateTime orderDateTime;
    private LocalDate expectedPickupDate;
    private LocalDateTime actualPickupDateTime;
    private Double shippingCost;
    private Double totalCost;

}
