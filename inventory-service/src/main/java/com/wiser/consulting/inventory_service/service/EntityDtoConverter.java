package com.wiser.consulting.inventory_service.service;

import org.springframework.stereotype.Service;

import com.wiser.consulting.inventory_service.dto.ProductDto;
import com.wiser.consulting.inventory_service.dto.ProductItemDto;
import com.wiser.consulting.inventory_service.entity.Product;
import com.wiser.consulting.inventory_service.entity.ProductItem;

@Service
public interface EntityDtoConverter {
    ProductDto contervtProducttoProductDto(Product product);
    Product contervtProductDtoToProduct(ProductDto productDto);
    ProductItem contervtProductItemDtoToProductItem(ProductItemDto productItemDto);
    ProductItemDto contervtProductItemToProductItemDto(ProductItem productItem);
}
