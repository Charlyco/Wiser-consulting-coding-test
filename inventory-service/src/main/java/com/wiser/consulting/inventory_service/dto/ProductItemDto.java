package com.wiser.consulting.inventory_service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductItemDto {
    private Long id;
    private String barcode;
    private String expiryDate;
    private Long productId;
    private String productUid;
}
