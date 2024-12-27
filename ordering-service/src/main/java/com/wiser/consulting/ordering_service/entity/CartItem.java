package com.wiser.consulting.ordering_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID uid;
    private String name;
    private String description;
    private Double price;
    private Set<String> images;
    private String category;
    private String brand;
    private String model;
    private String manufacturer;
    private String color;
    private Long weight;
    private Double discountPercentage;
    private Double discountPrice;
    private int quantity;

    void incrementQuantity() {
        this.quantity++;
    }
}
