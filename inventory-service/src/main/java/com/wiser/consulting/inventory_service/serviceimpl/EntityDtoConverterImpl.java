package com.wiser.consulting.inventory_service.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.wiser.consulting.inventory_service.dto.ProductDto;
import com.wiser.consulting.inventory_service.dto.ProductItemDto;
import com.wiser.consulting.inventory_service.entity.Product;
import com.wiser.consulting.inventory_service.entity.ProductItem;
import com.wiser.consulting.inventory_service.repo.ProductRepository;
import com.wiser.consulting.inventory_service.service.EntityDtoConverter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntityDtoConverterImpl implements EntityDtoConverter {
    private final ProductRepository productRepository;

    @Override
    public ProductDto contervtProducttoProductDto(Product product) {
        Set<ProductItemDto> productItemDtos = new HashSet<>();
        product.getProductItemList().forEach(productItem ->
                productItemDtos.add(contervtProductItemToProductItemDto(productItem)));

        return ProductDto.builder()
                .uid(String.valueOf(product.getUid()))
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .brand(product.getBrand())
                .model(product.getModel())
                .images(product.getImages())
                .color(product.getColor())
                .weight(product.getWeight())
                .category(product.getCategory())
                .manufacturer(product.getManufacturer())
                .branch(product.getBranch())
                .productItemDtoList(productItemDtos)
                .discountPercentage(product.getDiscountPercentage())
                .discountPrice(product.getDiscountedPrice())
                .quantitySold(product.getQuantitySold())
                .availableQuantity(product.getAvailableQuantity())
                .build();
    }

    @Override
    public Product contervtProductDtoToProduct(ProductDto productDto) {

        return Product.builder()
                .uid(UUID.randomUUID())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .brand(productDto.getBrand())
                .model(productDto.getModel())
                .color(productDto.getColor())
                .weight(productDto.getWeight())
                .images(productDto.getImages())
                .category(productDto.getCategory())
                .manufacturer(productDto.getManufacturer())
                .branch(productDto.getBranch())
                .productItemList(new HashSet<>())
                .discountPercentage(productDto.getDiscountPercentage())
                .discountPrice(productDto.getDiscountPrice())
                .availableQuantity(0L)
                .build();
    }

    @Override
    public ProductItem contervtProductItemDtoToProductItem(ProductItemDto productItemDto) {
        return ProductItem.builder()
                .barcode(productItemDto.getBarcode())
                .expiryDate(LocalDate.parse(productItemDto.getExpiryDate()))
                .product(productRepository.findByUid(UUID.fromString(productItemDto.getProductUid())).orElseThrow())
                .build();
    }

    @Override
    public ProductItemDto contervtProductItemToProductItemDto(ProductItem productItem) {
        return ProductItemDto.builder()
                .barcode(productItem.getBarcode())
                .expiryDate(String.valueOf(productItem.getExpiryDate()))
                .productUid(String.valueOf(productItem.getProduct().getUid()))
                .build();
    }
}
