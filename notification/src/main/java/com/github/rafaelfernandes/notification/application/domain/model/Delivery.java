package com.github.rafaelfernandes.notification.application.domain.model;

public record Delivery(
        DeliveryId id,
        String destinationName,
        String packageDescription,
        String residentName,
        String residentCellphone

) {
    public record DeliveryId(String id) {
    }
}

