package com.wiser.consulting.ordering_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.wiser.consulting.ordering_service.enums.CartStatus;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID cartUid;
    private String ownerEmail;
    private CartStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CartItem> cartItemSet;
    private Double totalPrice;

}
