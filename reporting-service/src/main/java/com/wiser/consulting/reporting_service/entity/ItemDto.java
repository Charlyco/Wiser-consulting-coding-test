package com.wiser.consulting.reporting_service.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ItemDto {
    @Id
    private String uid;
    private String name;
    private String description;
    private Double price;
    private String category;
    private String brand;
    private String model;
    private String manufacturer;
    private String color;
    private Long weight;
    private int quantity;

}
