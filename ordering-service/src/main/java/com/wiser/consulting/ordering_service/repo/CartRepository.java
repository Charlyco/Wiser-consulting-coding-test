package com.wiser.consulting.ordering_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wiser.consulting.ordering_service.entity.Cart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCartUid(UUID cartUid);

    Optional<List<Cart>> findCartByOwnerEmail(String ownerEmail);
}
