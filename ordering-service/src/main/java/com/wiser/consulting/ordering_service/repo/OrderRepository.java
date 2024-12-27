package com.wiser.consulting.ordering_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wiser.consulting.ordering_service.entity.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOwner_Email(String email);

    Optional<Order> findByOrderNumber(String orderNumber);

    Optional<List<Order>> findByCountry(String country);
}
