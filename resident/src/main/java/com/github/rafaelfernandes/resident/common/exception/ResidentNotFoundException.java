package com.github.rafaelfernandes.resident.common.exception;

public class ResidentNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Resident not found";

    private static final Integer STATUS = 404;

    public ResidentNotFoundException() {
        super(MESSAGE);
    }

    public Integer getStatus() {
        return STATUS;
    }

    public String getMessage() {
        return super.getMessage();
    }
}
