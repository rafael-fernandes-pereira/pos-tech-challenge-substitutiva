package com.github.rafaelfernandes.user.application.domain.model;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testEmployeeConstructor_ValidParameters() {
        String name = "John Doe";
        String document = "123.456.789-09";
        String cellphone = "+12 34 56789-1234";

        Employee employee = new Employee(name, document, cellphone);

        assertNotNull(employee.getEmployeeId());
        assertEquals(name, employee.getName());
        assertEquals(document, employee.getDocument());
        assertEquals(cellphone, employee.getCellphone());
    }

    @Test
    public void testEmployeeConstructor_InvalidDocument() {
        String name = "John Doe";
        String invalidDocument = "123456789";
        String cellphone = "+12 34 56789-1234";

        assertThrows(ConstraintViolationException.class, () -> {
            new Employee(name, invalidDocument, cellphone);
        });
    }

    @Test
    public void testEmployeeConstructor_InvalidCellphone() {
        String name = "John Doe";
        String document = "123.456.789-09";
        String invalidCellphone = "1234567890";

        assertThrows(ConstraintViolationException.class, () -> {
            new Employee(name, document, invalidCellphone);
        });
    }

    @Test
    public void testEmployeeOfMethod_ValidParameters() {
        String id = "123e4567-e89b-12d3-a456-426614174000";
        String name = "John Doe";
        String document = "123.456.789-09";
        String cellphone = "+12 34 56789-1234";

        Employee employee = Employee.of(id, name, document, cellphone);

        assertNotNull(employee.getEmployeeId());
        assertEquals(id, employee.getEmployeeId().id());
        assertEquals(name, employee.getName());
        assertEquals(document, employee.getDocument());
        assertEquals(cellphone, employee.getCellphone());
    }

    @Test
    public void testEmployeeOfMethod_InvalidDocument() {
        String id = "123e4567-e89b-12d3-a456-426614174000";
        String name = "John Doe";
        String invalidDocument = "123456789";
        String cellphone = "+12 34 56789-1234";

        assertThrows(ConstraintViolationException.class, () -> {
            Employee.of(id, name, invalidDocument, cellphone);
        });
    }

    @Test
    public void testEmployeeOfMethod_InvalidCellphone() {
        String id = "123e4567-e89b-12d3-a456-426614174000";
        String name = "John Doe";
        String document = "123.456.789-09";
        String invalidCellphone = "1234567890";

        assertThrows(ConstraintViolationException.class, () -> {
            Employee.of(id, name, document, invalidCellphone);
        });
    }
}