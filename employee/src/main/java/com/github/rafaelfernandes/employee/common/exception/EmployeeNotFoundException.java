package com.github.rafaelfernandes.employee.common.exception;

public class EmployeeNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Employee not found";

    private static final Integer STATUS = 404;

    public EmployeeNotFoundException() {
        super(MESSAGE);
    }

    public Integer getStatus() {
        return STATUS;
    }

    public String getMessage() {
        return super.getMessage();
    }
}
