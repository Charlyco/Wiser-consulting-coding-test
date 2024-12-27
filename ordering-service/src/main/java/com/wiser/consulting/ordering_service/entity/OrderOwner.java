package com.wiser.consulting.ordering_service.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderOwner(String fullName, String email, String uid, String address, String phoneNumber) {
}
