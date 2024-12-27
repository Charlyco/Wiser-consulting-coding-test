package com.wiser.consulting.inventory_service.entity;

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
public class Product {
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
    private String branch;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ProductItem> productItemList;
    private Double discountPercentage;
    private Double discountPrice;
    private Long quantitySold;
    private Long availableQuantity;


    public boolean addProductItem(ProductItem productItem) {
        return productItemList.add(productItem);
    }

    public void removeProductItem(ProductItem productItem) {
        productItemList.remove(productItem);
    }

    public boolean removeProductItems(Set<ProductItem> productItems) {
        if (getQuantitySold() == null) {
            setQuantitySold((long) productItems.size());
        }else {
            setQuantitySold(getQuantitySold() + productItems.size());
        }
        return productItemList.removeAll(productItems);
    }

    public boolean addItemImage(String imageUrl) {
       return images.add(imageUrl);
    }

    public boolean removeItemImage(String imageUrl) {
        return images.remove(imageUrl);
    }

    public Double getDiscountedPrice() {
        return price - ((discountPercentage/100.0) * price);
    }

    public synchronized void decreamentAvailableQuantity(int quantity) {
        availableQuantity -= quantity;
    }
}
