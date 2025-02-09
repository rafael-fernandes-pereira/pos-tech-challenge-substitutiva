package com.github.rafaelfernandes.delivery.common.exception;

public class DeliveryNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Delivery not found";

    private static final Integer STATUS = 404;

    public DeliveryNotFoundException() {
        super(MESSAGE);
    }

    public Integer getStatus() {
        return STATUS;
    }

    public String getMessage() {
        return super.getMessage();
    }
}
