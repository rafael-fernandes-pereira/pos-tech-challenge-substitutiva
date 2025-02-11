package com.github.rafaelfernandes.notification.common.exception;

public class DeliveryNotFoundException extends RuntimeException {

    private static final Integer STATUS = 404;

    public DeliveryNotFoundException(String deliveryId) {
        super(String.format("Delivery %s not found", deliveryId));
    }

    public Integer getStatus() {
        return STATUS;
    }
}
