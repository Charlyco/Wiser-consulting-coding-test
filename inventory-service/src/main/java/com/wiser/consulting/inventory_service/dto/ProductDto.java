package com.wiser.consulting.inventory_service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class ProductDto {
    private Long id;
    private String uid;
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
    private String branch;
    private Set<ProductItemDto> productItemDtoList;
    private Double discountPercentage;
    private Double discountPrice;
    private Long quantitySold;
    private Long availableQuantity;
}
