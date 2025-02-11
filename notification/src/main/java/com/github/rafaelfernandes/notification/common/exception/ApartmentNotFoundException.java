package com.github.rafaelfernandes.notification.common.exception;

public class ApartmentNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Resident not found";

    private static final Integer STATUS = 404;

    public ApartmentNotFoundException() {
        super(MESSAGE);
    }

    public Integer getStatus() {
        return STATUS;
    }

    public String getMessage() {
        return MESSAGE;
    }
}
