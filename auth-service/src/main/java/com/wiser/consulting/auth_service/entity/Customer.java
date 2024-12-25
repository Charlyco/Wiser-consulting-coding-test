package com.wiser.consulting.auth_service.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "customer_id")
@DiscriminatorValue("C")
@Setter
@Getter
public class Customer extends User {
    private LocalDateTime createdAt;
    private String permanentAddress;

}
