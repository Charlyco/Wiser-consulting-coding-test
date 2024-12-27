package com.wiser.consulting.inventory_service.serviceimpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.wiser.consulting.inventory_service.dto.ApiResponse;
import com.wiser.consulting.inventory_service.dto.PaginationApiResponse;
import com.wiser.consulting.inventory_service.dto.ProductDto;
import com.wiser.consulting.inventory_service.dto.ProductItemDto;
import com.wiser.consulting.inventory_service.entity.Product;
import com.wiser.consulting.inventory_service.entity.ProductItem;
import com.wiser.consulting.inventory_service.repo.ProductItemRepository;
import com.wiser.consulting.inventory_service.repo.ProductRepository;
import com.wiser.consulting.inventory_service.service.EntityDtoConverter;
import com.wiser.consulting.inventory_service.service.InventoryService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {
    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final EntityDtoConverter entityDtoConverter;

    private static final int MAX_PAGE_SIZE = 50; // Maximum page size limit
    private static final int DEFAULT_PAGE_SIZE = 10; // Default page size
    private boolean shouldWait;
    private Condition isLocked;
    private Lock stockLock;

    InventoryServiceImpl(ProductRepository productRepository, ProductItemRepository productItemRepository, EntityDtoConverter entityDtoConverter) {
        this.productRepository = productRepository;
        this.productItemRepository = productItemRepository;
        this.entityDtoConverter = entityDtoConverter;
        shouldWait = false;
        stockLock = new ReentrantLock();
        isLocked = stockLock.newCondition();
    }

    @Override
    public ApiResponse addProduct(ProductDto productDto, HttpServletRequest httpServletRequest) {
        Product newProduct = productRepository.save(entityDtoConverter.contervtProductDtoToProduct(productDto));
        log.info("New product created: {} at {}", newProduct, LocalDateTime.now());
        return new ApiResponse("Product added successfully", 200, true,
                entityDtoConverter.contervtProducttoProductDto(newProduct));
    }

    @Override
    public ApiResponse addProductItems(String productUid, List<ProductItemDto> productItemDtos, HttpServletRequest httpServletRequest) {
        Product product = productRepository.findByUid(UUID.fromString(productUid)).orElseThrow();
        productItemDtos.forEach(productItemDto -> {
            log.info("{}, {}", productUid, productItemDto.getProductUid());
            ProductItem newItem = productItemRepository.save(entityDtoConverter.contervtProductItemDtoToProductItem(productItemDto));
            product.addProductItem(newItem);
            product.increamentAvailableQuantity();
            productRepository.save(product);
        });
        log.info("New items added to product: {} at {}", product.getName(), LocalDateTime.now());
        return new ApiResponse("Items added successfully", 200, true, entityDtoConverter.contervtProducttoProductDto(product));

    }

    @Override
    public Boolean checkProductStock(String productId, int quantity, HttpServletRequest httpServletRequest) {
        try {
            while (shouldWait) {
                isLocked.await();
            }
            Product product = productRepository.findByUid(UUID.fromString(productId)).orElseThrow();
            if (product.getProductItemList().size()>= quantity) {
                return true;
            }else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ApiResponse updatProductStock(String productId, int quantity, HttpServletRequest httpServletRequest) {
        stockLock.lock();
        shouldWait = true;
        try {
            Product product = productRepository.findByUid(UUID.fromString(productId)).orElseThrow();
            if (product.getAvailableQuantity() >= quantity) {
                product.decreamentAvailableQuantity(quantity);
                isLocked.signalAll();
                return new ApiResponse("Stock for: {} updated", HttpStatus.SC_OK, true, entityDtoConverter.contervtProducttoProductDto(product));
            }else {
                return new ApiResponse("Unable to update stock", HttpStatus.SC_OK, false, null);
            }
        } catch (Exception e) {
            log.info("An error has occurred: {}", e.getLocalizedMessage());
            return new ApiResponse("An error occurred", HttpStatus.SC_NOT_MODIFIED, false, e);
        }finally {
            shouldWait = false;
            stockLock.unlock();
        }
    }
    

    @Override
    public ApiResponse deleteProduct(String productId, HttpServletRequest httpServletRequest) {
        if (productRepository.findByUid(UUID.fromString(productId)).isPresent()) {
            Product product = productRepository.findByUid(UUID.fromString(productId)).orElseThrow();
            productRepository.delete(product);
            log.info("Product: {} deleted successfully at {}", product.getName(), LocalDateTime.now());
            return new ApiResponse("Product deleted successfully", 200, true, null);
        }else {
            return new ApiResponse("Product not found", 400, false, null);
        }
    }

    @Override
    public ApiResponse removeProductItem(String productBarCode, HttpServletRequest httpServletRequest) {
        try {
            String accessToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            
                ProductItem productItem = productItemRepository.findByBarcode(productBarCode).orElseThrow();
            Product product = productRepository.findByUid(productItem.getProduct().getUid()).orElseThrow();

            product.removeProductItem(productItem);
            productRepository.save(product);
            productItemRepository.delete(productItem);
            log.info("The stock of {} has been updated at {}", product.getName(), LocalDateTime.now());
            shouldWait = false;
            isLocked.signalAll();
            return new ApiResponse(
            "Product items stock updated successfully", 
                HttpStatus.SC_OK, 
                true, 
                entityDtoConverter.contervtProducttoProductDto(product));
            
        } catch (Exception e) {
            log.info("An error has occurred: {}", e.getLocalizedMessage());
            return new ApiResponse("An error occurred", HttpStatus.SC_NOT_MODIFIED, false, e);
        }
    }

    @Override
    public PaginationApiResponse filterByCategory(String category, int page, int size, HttpServletRequest httpServletRequest) {
        int effectivePageSize = size;
        if (size == 0 || size > MAX_PAGE_SIZE) {
            effectivePageSize = DEFAULT_PAGE_SIZE;
        }
        Pageable pageable = PageRequest.of(page, effectivePageSize);
        Page<Product> productPage = productRepository.findByCategory(category, pageable).orElseThrow();
        return getPaginationApiResponse(productPage);
    }

    @Override
    public PaginationApiResponse filterByName(String name, int page, int size, HttpServletRequest httpServletRequest) {
        int effectivePageSize = size;
        if (size == 0 || size > MAX_PAGE_SIZE) {
            effectivePageSize = DEFAULT_PAGE_SIZE;
        }
        Pageable pageable = PageRequest.of(page, effectivePageSize);
        Page<Product> productPage = productRepository.findByName(name, pageable).orElseThrow();
        log.info(productPage.getContent().toString());

        return getPaginationApiResponse(productPage);
    }

    @Override
    public PaginationApiResponse filterByManufacturer(String manufacturer, int page, int size, HttpServletRequest httpServletRequest) {
        int effectivePageSize = size;
        if (size == 0 || size > MAX_PAGE_SIZE) {
            effectivePageSize = DEFAULT_PAGE_SIZE;
        }
        Pageable pageable = PageRequest.of(page, effectivePageSize);
        Page<Product> productPage = productRepository.findByManufacturer(manufacturer, pageable).orElseThrow();
        return getPaginationApiResponse(productPage);
    }

    @Override
    public ApiResponse applyDiscount(String productUid, Double discountPercentage, HttpServletRequest httpServletRequest) {
        String accessToken = httpServletRequest.getHeader("Authorization");
        String employee = getUserByAccessToken(accessToken);
            Product product = productRepository.findByUid(UUID.fromString(productUid)).orElseThrow();
            product.setDiscountPercentage(discountPercentage);
            productRepository.save(product);
            log.info("Discount applied on {} by {} at {}", product.getName(), employee, LocalDateTime.now());
            return new ApiResponse(
                    "Discount applied successfully", 
                    HttpStatus.SC_OK, 
                    true, 
                    entityDtoConverter.contervtProducttoProductDto(product));
                
        }
        
            
        
    @Override
    public ApiResponse updateProductPrice(String productId, Double newPrice, HttpServletRequest httpServletRequest) {
        String accessToken = httpServletRequest.getHeader("Authorization");
        String employee = getUserByAccessToken(accessToken);
        Product product = productRepository.findByUid(UUID.fromString(productId)).orElseThrow();
        product.setPrice(newPrice);
        productRepository.save(product);
        log.info("Price of {} updated by {} at {}", product.getName(), employee, LocalDateTime.now());
        return new ApiResponse(
            "Price updated Successfully",
             HttpStatus.SC_OK, 
             true, 
             entityDtoConverter.contervtProducttoProductDto(product));

    }

    private PaginationApiResponse getPaginationApiResponse(Page<Product> productPage) {
        List<ProductDto> productDtos = new ArrayList<>();
        log.info(productPage.getContent().toString());
        productPage.getContent().forEach(product -> {
            productDtos.add(entityDtoConverter.contervtProducttoProductDto(product));
        });
        return PaginationApiResponse.builder()
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .data(productDtos)
                .build();
    }
        

    @CircuitBreaker(name = "getUserByAccessToken", fallbackMethod = "getUserByAccessTokenFallback")
    private String getUserByAccessToken(String accessToken) {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8084/")
                .build();

        ApiResponse apiResponse = restClient.get()
                                .uri("auth-service/api/v1/auth/accessToken")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .retrieve()
                                .body(ApiResponse.class);

        return (String) apiResponse.data();
    }

    private String getUserByAccessTokenFallback() {
        return "";
    }

}
