package com.github.rafaelfernandes.common.exceptions;

public class UnauthorizedException extends RuntimeException {

    private static final String MESSAGE = "Invalid username or password";

    private static final Integer STATUS = 403;

    public UnauthorizedException() {
        super(MESSAGE);
    }

    public Integer getStatus() {
        return STATUS;
    }

    public String getMessage() {
        return MESSAGE;
    }
}
