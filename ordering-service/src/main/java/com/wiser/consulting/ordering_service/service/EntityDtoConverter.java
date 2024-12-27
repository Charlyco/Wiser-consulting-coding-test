package com.wiser.consulting.ordering_service.service;

import org.springframework.stereotype.Service;

import com.wiser.consulting.ordering_service.dto.CartDto;
import com.wiser.consulting.ordering_service.dto.ItemDto;
import com.wiser.consulting.ordering_service.dto.NewOrderDto;
import com.wiser.consulting.ordering_service.dto.OrderDto;
import com.wiser.consulting.ordering_service.entity.Cart;
import com.wiser.consulting.ordering_service.entity.CartItem;
import com.wiser.consulting.ordering_service.entity.Order;

@Service
public interface EntityDtoConverter {
    Order convertOrderDtoToOrder(NewOrderDto orderDto);
    OrderDto convertOrderToOrderDto(Order order);
    Cart convertCartDtoToCart(CartDto cartDto);
    CartDto convertCartToCartDto(Cart cart);
    ItemDto convertCartItemToItemDto(CartItem cartItem);
}
