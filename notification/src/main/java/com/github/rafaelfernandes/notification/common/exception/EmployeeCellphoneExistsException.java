package com.github.rafaelfernandes.notification.common.exception;

public class EmployeeCellphoneExistsException extends RuntimeException {

    private static final String MESSAGE = "Employee with cellphone %s already exists";

    private static final Integer STATUS = 409;

    public EmployeeCellphoneExistsException(String cellphone) {
        super(String.format(MESSAGE, cellphone));
    }

    public Integer getStatus() {
        return STATUS;
    }


}
