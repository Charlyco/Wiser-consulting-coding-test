package com.wiser.consulting.inventory_service.controllerimpl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.wiser.consulting.inventory_service.controller.InventoryController;
import com.wiser.consulting.inventory_service.dto.ApiResponse;
import com.wiser.consulting.inventory_service.dto.PaginationApiResponse;
import com.wiser.consulting.inventory_service.dto.ProductDto;
import com.wiser.consulting.inventory_service.dto.ProductItemDto;
import com.wiser.consulting.inventory_service.service.InventoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InventoryControllerImpls implements InventoryController{
    private final InventoryService inventoryService;
    private final HttpServletRequest request;
    
    @Override
    public ResponseEntity<ApiResponse> addProduct(ProductDto productDto) {
        return ResponseEntity.ok(inventoryService.addProduct(productDto, request));
    }

    @Override
    public ResponseEntity<ApiResponse> addProductItems(String productUid, List<ProductItemDto> productItemDtos) {
        return ResponseEntity.ok(inventoryService.addProductItems(productUid, productItemDtos, request));
    }

    @Override
    public ResponseEntity<Boolean> checkProductStock(String productId, int quantity) {
        return ResponseEntity.ok(inventoryService.checkProductStock(productId, quantity, request));
    }

    @Override
    public ResponseEntity<ApiResponse> updatProductStock(String productId, int quantity) {
        return ResponseEntity.ok(inventoryService.updatProductStock(productId, quantity, request));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteProduct(String productId) {
        return ResponseEntity.ok(inventoryService.deleteProduct(productId, request));
    }

    @Override
    public ResponseEntity<ApiResponse> removeProductItem(String productBarCode) {
        return ResponseEntity.ok(inventoryService.removeProductItem(productBarCode, request));
    }

    @Override
    public ResponseEntity<PaginationApiResponse> filterByCategory(String category, int page, int size) {
        return ResponseEntity.ok(inventoryService.filterByCategory(category, page, size, request));
    }

    @Override
    public ResponseEntity<PaginationApiResponse> filterByName(String name, int page, int size) {
        return ResponseEntity.ok(inventoryService.filterByName(name, page, size, request));
    }

    @Override
    public ResponseEntity<PaginationApiResponse> filterByManufacturer(String manufacturer, int page, int size) {
        return ResponseEntity.ok(inventoryService.filterByManufacturer(manufacturer, page, size, request));
    }

    @Override
    public ResponseEntity<ApiResponse> applyDiscount(String productUid, Double discountPercentage) {
        return ResponseEntity.ok(inventoryService.applyDiscount(productUid, discountPercentage, request));
    }

    @Override
    public ResponseEntity<ApiResponse> updateProductPrice(String productId, Double newPrice) {
        return ResponseEntity.ok(inventoryService.updateProductPrice(productId, newPrice, request));
    }

}
