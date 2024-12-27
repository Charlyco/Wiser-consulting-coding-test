package com.wiser.consulting.inventory_service.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wiser.consulting.inventory_service.entity.Product;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByUid(UUID uid);

    Optional<Page<Product>> findByCategory(String category, Pageable pageable);

    @Query("SELECT p from Product p where p.name like %:name%")
    Optional<Page<Product>> findByName(String name, Pageable pageable);

    @Query("SELECT p from Product p where p.manufacturer like %:manufacturer%")
    Optional<Page<Product>> findByManufacturer(String manufacturer, Pageable pageable);
}
