package com.wiser.consulting.ordering_service.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ItemDto {
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
