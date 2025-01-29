package com.github.rafaelfernandes.common.exceptions;

public class InvalidTokenException extends RuntimeException {

    private static final String MESSAGE = "Invalid Token";

    private static final Integer STATUS = 403;

    public InvalidTokenException() {
        super(MESSAGE);
    }

    public Integer getStatus() {
        return STATUS;
    }

    public String getMessage() {
        return MESSAGE;
    }
}
