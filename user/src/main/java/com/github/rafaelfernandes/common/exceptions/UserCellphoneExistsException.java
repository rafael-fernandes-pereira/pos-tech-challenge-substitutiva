package com.github.rafaelfernandes.common.exceptions;

public class UserCellphoneExistsException extends RuntimeException {

    private static final String MESSAGE = "User with cellphone %s already exists";

    private static final Integer STATUS = 409;

    public UserCellphoneExistsException(String cellphone)  {
        super(String.format(MESSAGE, cellphone));
    }

    public Integer getStatus() {
        return STATUS;
    }
}
