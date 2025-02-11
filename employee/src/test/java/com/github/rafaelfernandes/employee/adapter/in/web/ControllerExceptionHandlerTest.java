package com.github.rafaelfernandes.employee.adapter.in.web;

import com.github.rafaelfernandes.employee.adapter.in.web.response.ResponseError;
import com.github.rafaelfernandes.employee.common.exception.EmployeeCellphoneExistsException;
import com.github.rafaelfernandes.employee.common.exception.EmployeeNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler controllerExceptionHandler;

    @BeforeEach
    public void setUp() {
        controllerExceptionHandler = new ControllerExceptionHandler();
    }

    @Test
    public void testErrorValidation_ValidationException() {
        ValidationException exception = new ValidationException("Validation error");
        ResponseEntity<ResponseError> response = controllerExceptionHandler.errorValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error", response.getBody().message());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
    }

    @Test
    public void testErrorValidation_EmployeeCellphoneExistsException() {

        String cellPhone = "+11 23 98765-0972";

        EmployeeCellphoneExistsException exception = new EmployeeCellphoneExistsException(cellPhone);
        ResponseEntity<ResponseError> response = controllerExceptionHandler.errorValidation(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Employee with cellphone +11 23 98765-0972 already exists", response.getBody().message());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().status());
    }

    @Test
    public void testErrorValidation_EmployeeNotFoundException() {
        EmployeeNotFoundException exception = new EmployeeNotFoundException();
        ResponseEntity<ResponseError> response = controllerExceptionHandler.errorValidation(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
    }
}
