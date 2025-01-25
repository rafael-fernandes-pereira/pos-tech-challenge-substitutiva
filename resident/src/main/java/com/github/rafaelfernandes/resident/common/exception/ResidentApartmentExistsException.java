package com.github.rafaelfernandes.resident.common.exception;

public class ResidentApartmentExistsException extends RuntimeException {

    private static final String MESSAGE = "Resident with apartment %s already exists";

    private static final Integer STATUS = 409;

    public ResidentApartmentExistsException(Integer apartment) {
        super(String.format(MESSAGE, apartment));
    }

    public Integer getStatus() {
        return STATUS;
    }

    public String getMessage() {
        return super.getMessage();
    }
}
