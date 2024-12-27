package com.wiser.consulting.ordering_service.dto;

public record OrderStatusNotification(String status, String orderNumber, String description, String country, String ownerUid) {

}
