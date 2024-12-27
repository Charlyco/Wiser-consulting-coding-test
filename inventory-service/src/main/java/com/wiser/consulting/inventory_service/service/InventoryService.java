package com.wiser.consulting.inventory_service.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import com.wiser.consulting.inventory_service.dto.ApiResponse;
import com.wiser.consulting.inventory_service.dto.PaginationApiResponse;
import com.wiser.consulting.inventory_service.dto.ProductDto;
import com.wiser.consulting.inventory_service.dto.ProductItemDto;

import java.util.List;

@Service
public interface InventoryService {
    ApiResponse addProduct(ProductDto productDto, HttpServletRequest httpServletRequest);

    ApiResponse addProductItems(String productUid, List<ProductItemDto> productItemDtos, HttpServletRequest httpServletRequest);

    Boolean checkProductStock(String productId, int quantity, HttpServletRequest httpServletRequest);
    ApiResponse updatProductStock(String productId, int quantity, HttpServletRequest httpServletRequest);

    ApiResponse deleteProduct(String productId, HttpServletRequest httpServletRequest);

    ApiResponse removeProductItem(String productBarCode, HttpServletRequest httpServletRequest);

    PaginationApiResponse filterByCategory(String category, int page, int size, HttpServletRequest httpServletRequest);

    PaginationApiResponse filterByName(String name, int page, int size, HttpServletRequest httpServletRequest);

    PaginationApiResponse filterByManufacturer(String manufacturer, int page, int size, HttpServletRequest httpServletRequest);

    ApiResponse applyDiscount(String productUid, Double discountPercentage, HttpServletRequest httpServletRequest);
    ApiResponse updateProductPrice(String productId, Double newPrice, HttpServletRequest httpServletRequest);

}
