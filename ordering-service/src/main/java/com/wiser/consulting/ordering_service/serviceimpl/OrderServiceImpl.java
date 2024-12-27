package com.wiser.consulting.ordering_service.serviceimpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.wiser.consulting.ordering_service.config.KafkaTopics;
import com.wiser.consulting.ordering_service.dto.ApiResponse;
import com.wiser.consulting.ordering_service.dto.CartDto;
import com.wiser.consulting.ordering_service.dto.NewOrderDto;
import com.wiser.consulting.ordering_service.dto.OrderDto;
import com.wiser.consulting.ordering_service.entity.Cart;
import com.wiser.consulting.ordering_service.entity.CartItem;
import com.wiser.consulting.ordering_service.entity.Order;
import com.wiser.consulting.ordering_service.repo.CartRepository;
import com.wiser.consulting.ordering_service.repo.OrderRepository;
import com.wiser.consulting.ordering_service.service.EntityDtoConverter;
import com.wiser.consulting.ordering_service.service.OrderService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final EntityDtoConverter entityDtoConverter;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public ApiResponse saveOrder(NewOrderDto orderDto, HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        Order newOrder = entityDtoConverter.convertOrderDtoToOrder(orderDto);
        
        Set<CartItem> itemsInCart = newOrder.getCart().getCartItemSet();
        boolean isAllProductsInSctock = itemsInCart.stream().allMatch( item -> 
            isItemInStock(item.getUid(), item.getQuantity(), accessToken)
        );

        if (isAllProductsInSctock) {
            orderRepository.save(newOrder);
            itemsInCart.forEach( item ->
                updateInventory(item.getUid(), item.getQuantity(), accessToken)
            );

            kafkaTemplate.send(KafkaTopics.ORDER_REPORT, newOrder.getOrderNumber(), orderDto);

            log.info("New order: {} at {}", newOrder, LocalDateTime.now());

            return new ApiResponse(
                "Order successfully created",
                HttpStatus.SC_CREATED,
                true,
                entityDtoConverter.convertOrderToOrderDto(newOrder));
        }else {
            return new ApiResponse("Some items are out of stock", HttpStatus.SC_NOT_FOUND, false, null);
        }
    }

    @Override
    public ApiResponse getAlOrders(HttpServletRequest request) {
        String country = request.getHeader("country");
        String accessToken = request.getHeader("Authorization");
        Boolean isAuthorized = isUserAuthorized(accessToken);
        if (isAuthorized) {
            List<Order> orderList = orderRepository.findByCountry(country).orElseThrow();
            List<OrderDto> orderDtoList = new ArrayList<>();
            orderList.forEach(order -> orderDtoList.add(entityDtoConverter.convertOrderToOrderDto(order)));
            return new ApiResponse("Success", HttpStatus.SC_OK, true, orderDtoList);
        }else {
            return new ApiResponse("Access denied", HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, false, null);
        }
    }

    @Override
    public ApiResponse getSingleOrderDetail(String orderNumber, HttpServletRequest request) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow();
        return new ApiResponse("Success", HttpStatus.SC_OK, true, entityDtoConverter.convertOrderToOrderDto(order));
    }

    @Override
    public ApiResponse createCart(CartDto cartDto, HttpServletRequest request) {
        String country = request.getHeader("country");
        Cart newCart = entityDtoConverter.convertCartDtoToCart(cartDto);
        cartRepository.save(newCart);
        log.info("New cart for {} at {}", country, LocalDateTime.now());
        return new ApiResponse("Cart created successfully", HttpStatus.SC_CREATED, true, entityDtoConverter.convertCartToCartDto(newCart));
    }

    @Override
    public ApiResponse updateCart(String cartId, CartDto cartDto, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        String userEmail = getUserEmailFromToken(accessToken);
        Cart cart = cartRepository.findByCartUid(UUID.fromString(cartId)).orElseThrow();
        if (userEmail.equals(cart.getOwnerEmail())) {
            cart.setCartItemSet(cartDto.getCartItemSet());
            cart.setLastUpdatedAt(LocalDateTime.now());
            cart.setTotalPrice(calculateCartTotalPrice(cartDto.getCartItemSet()));
            cartRepository.save(cart);
            log.info("Cart updated successfully by {} at {}", userEmail, LocalDateTime.now());
            return new ApiResponse("Cart updated successfully", HttpStatus.SC_OK, true, entityDtoConverter.convertCartToCartDto(cart));
        }else return new ApiResponse("Unauthorized operation", HttpStatus.SC_NOT_FOUND, false, null);
    }

    private Double calculateCartTotalPrice(Set<CartItem> cartItemSet) {
        double total = 0.0;
        for (CartItem cartItem : cartItemSet) {
            total += cartItem.getPrice() * cartItem.getQuantity();
        }
        return total;
    }

    @CircuitBreaker(name = "getUserEmailFromToken", fallbackMethod = "getUserEmailFromTokenFallback")
    private String getUserEmailFromToken(String accessToken) {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8084/")
                .build();

        ApiResponse apiResponse = restClient.get()
                .uri("auth-service/api/v1/auth/accessToken")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .retrieve()
                .body(ApiResponse.class);

        if (apiResponse.status()) {
            return (String) apiResponse.data();
        }else return null;
    }

    @CircuitBreaker(name = "isUserAuthorized", fallbackMethod = "isUserAuthorizedFallback")
    private Boolean isUserAuthorized(String accessToken) {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8084/")
                .build();

        return restClient.get()
                .uri("auth-service/api/v1/user/isInventoryAdmin")
                .header("Authorization", accessToken)
                .retrieve()
                .body(Boolean.class);
    }

    @CircuitBreaker(name = "isItemInStock", fallbackMethod = "isItemInStockFallback")
    private Boolean isItemInStock(UUID productId, int quantity, String accessToken) {
        RestClient restClient = RestClient.builder()
                                .baseUrl("http://localhost:8084/")
                                .build();

        return restClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("inventory-service/api/v1/{productId}")
                                .queryParam("quantity", quantity)
                                .build(productId.toString()))
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, accessToken)
                        .retrieve()
                        .body(Boolean.class);
    }

    @CircuitBreaker(name = "updateInventory", fallbackMethod = "updateInventoryFallback")
    private ApiResponse updateInventory(UUID productId, int quantity, String accessToken) {
        RestClient restClient = RestClient.builder()
                                .baseUrl("http://localhost:8084/")
                                .build();

        return restClient.put()
                        .uri(uriBuilder -> uriBuilder
                                .path("inventory-service/api/v1/{productId}")
                                .queryParam("quantity", quantity)
                                .build(productId.toString()))
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, accessToken)
                        .retrieve()
                        .body(ApiResponse.class);
    }

    private Boolean isUserAuthorizedFallback() {
        return false;
    }

    private String getUserEmailFromTokenFallback() {
        return null;
    }

    private Boolean isItemInStockFallback() {
        return false;
    }

    private ApiResponse updateInventoryFallback() {
        return new ApiResponse("Service unavailable", 0, false, null);
    }
}
