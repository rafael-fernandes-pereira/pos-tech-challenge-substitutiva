package com.github.rafaelfernandes.common.exceptions;

public class ResidentCellphoneExistsException extends RuntimeException {

    private static final String MESSAGE = "Resident with cellphone %s already exists";

    private static final Integer STATUS = 409;

    public ResidentCellphoneExistsException(String cellphone)  {
        super(String.format(MESSAGE, cellphone));
    }

    public Integer getStatus() {
        return STATUS;
    }
}
