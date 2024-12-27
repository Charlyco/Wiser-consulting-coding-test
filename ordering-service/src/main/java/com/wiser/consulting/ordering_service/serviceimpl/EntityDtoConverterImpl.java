package com.wiser.consulting.ordering_service.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.wiser.consulting.ordering_service.dto.CartDto;
import com.wiser.consulting.ordering_service.dto.NewOrderDto;
import com.wiser.consulting.ordering_service.dto.OrderDto;
import com.wiser.consulting.ordering_service.entity.Cart;
import com.wiser.consulting.ordering_service.entity.CartItem;
import com.wiser.consulting.ordering_service.entity.Order;
import com.wiser.consulting.ordering_service.enums.CartStatus;
import com.wiser.consulting.ordering_service.enums.OrderStatus;
import com.wiser.consulting.ordering_service.repo.CartRepository;
import com.wiser.consulting.ordering_service.service.EntityDtoConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntityDtoConverterImpl implements EntityDtoConverter {
    private final CartRepository cartRepository;

    @Override
    public Order convertOrderDtoToOrder(NewOrderDto orderDto) {
        Cart cart = cartRepository.findByCartUid(UUID.fromString(orderDto.getCartUid())).orElseThrow();
        Double shippingCost = calculateShippingCost(cart, orderDto.getPickupAddress());
        Double totalCost = shippingCost + cart.getTotalPrice();

        return Order.builder()
                .orderNumber(generateOrderNumber())
                .owner(orderDto.getOwner())
                .pickupAddress(orderDto.getPickupAddress())
                .orderDateTime(LocalDateTime.now())
                .expectedPickupDate(LocalDate.now().plusDays(2))
                .status(OrderStatus.PENDING)
                .cart(cart)
                .shippingCost(shippingCost)
                .totalCost(totalCost)
                .build();
    }

    private Double calculateShippingCost(Cart itemCart, String pickupAddress) {
        // To be implemented
        return 100.0;
    }

    private String generateOrderNumber() {
        Random random = new Random();
        return String.valueOf((1000000000L + (long) (random.nextDouble() * 9000000000L)));
    }

    @Override
    public OrderDto convertOrderToOrderDto(Order order) {
        return OrderDto.builder()
                .orderNumber(order.getOrderNumber())
                .owner(order.getOwner())
                .pickupAddress(order.getPickupAddress())
                .status(order.getStatus())
                .orderDateTime(String.valueOf(order.getOrderDateTime()))
                .expectedPickupDate(String.valueOf(order.getExpectedPickupDate()))
                .actualPickupDateTime(String.valueOf(order.getActualPickupDateTime()))
                .cartDto(convertCartToCartDto(order.getCart()))
                .shippingCost(order.getShippingCost())
                .totalCost(order.getTotalCost())
                .build();
    }

    @Override
    public Cart convertCartDtoToCart(CartDto cartDto) {
        return Cart.builder()
                .cartUid(UUID.randomUUID())
                .cartItemSet(cartDto.getCartItemSet())
                .createdAt(LocalDateTime.parse(cartDto.getCreatedAt()))
                .ownerEmail(cartDto.getOwnerEmail())
                .lastUpdatedAt(LocalDateTime.parse(cartDto.getLastUpdatedAt()))
                .status(CartStatus.PENDING)
                .totalPrice(calculateCartCost(cartDto.getCartItemSet()))
                .build();
    }

    private Double calculateCartCost(Set<CartItem> cartItemSet) {
        double total = 0.0;
        for (CartItem cartItem : cartItemSet) {
            total += cartItem.getPrice() * cartItem.getQuantity();
        }
        return total;
    }

    @Override
    public CartDto convertCartToCartDto(Cart cart) {
        return CartDto.builder()
                .cartId(String.valueOf(cart.getCartUid()))
                .cartItemSet(cart.getCartItemSet())
                .createdAt(String.valueOf(cart.getCreatedAt()))
                .ownerEmail(cart.getOwnerEmail())
                .lastUpdatedAt(String.valueOf(cart.getLastUpdatedAt()))
                .status(String.valueOf(cart.getStatus()))
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}
