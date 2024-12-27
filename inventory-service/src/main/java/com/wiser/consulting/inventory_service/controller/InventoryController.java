package com.wiser.consulting.inventory_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wiser.consulting.inventory_service.dto.ApiResponse;
import com.wiser.consulting.inventory_service.dto.PaginationApiResponse;
import com.wiser.consulting.inventory_service.dto.ProductDto;
import com.wiser.consulting.inventory_service.dto.ProductItemDto;

@RestController
@RequestMapping("inventory-service/api/v1/")
public interface InventoryController {
    @PostMapping("product")
    ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto);

    @PostMapping("{productId}/items")
    ResponseEntity<ApiResponse> addProductItems(@PathVariable("productId") String productUid, @RequestBody List<ProductItemDto> productItemDtos);

    @GetMapping("{productId}")
    ResponseEntity<Boolean> checkProductStock(@PathVariable("productId") String productId, @RequestParam("quantity") int quantity);

    @PutMapping("{productId}")
    ResponseEntity<ApiResponse> updatProductStock(@PathVariable("productId") String productId, @RequestParam("quantity") int quantity);

    @DeleteMapping("{productId}")
    ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productId") String productId);

    @PutMapping("item")
    ResponseEntity<ApiResponse> removeProductItem(@RequestParam("barcode") String productBarCode);

    @GetMapping("catergory")
    ResponseEntity<PaginationApiResponse> filterByCategory(@RequestParam("category") String category, @RequestParam("page") int page, @RequestParam("size") int size);

    @GetMapping("name")
    ResponseEntity<PaginationApiResponse> filterByName(@RequestParam("name") String name, @RequestParam("page") int page, @RequestParam("size") int size);

    ResponseEntity<PaginationApiResponse> filterByManufacturer(@RequestParam("manufacturer") String manufacturer, @RequestParam("page") int page, @RequestParam("size") int size);

    @PutMapping("{productId}/discount")
    ResponseEntity<ApiResponse> applyDiscount(@PathVariable("productId") String productUid, @RequestParam("discount") Double discountPercentage);

    @PutMapping("{productId}/price")
    ResponseEntity<ApiResponse> updateProductPrice(@PathVariable("productId") String productId, @RequestParam("newPrice") Double newPrice);
}
