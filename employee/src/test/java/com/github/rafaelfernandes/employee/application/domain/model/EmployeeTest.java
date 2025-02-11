package com.github.rafaelfernandes.employee.application.domain.model;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("John Doe", "297.957.120-20", "+55 11 91234-5678");
    }

    @Test
    void testEmployeeCreation() {
        assertNotNull(employee);
        assertNotNull(employee.getEmployeeId());
        assertEquals("John Doe", employee.getName());
        assertEquals("297.957.120-20", employee.getDocument());
        assertEquals("+55 11 91234-5678", employee.getCellphone());
    }

    @Test
    void testEmployeeIdCreation() {
        Employee.EmployeeId employeeId = new Employee.EmployeeId("550e8400-e29b-41d4-a716-446655440000");
        assertNotNull(employeeId);
        assertEquals("550e8400-e29b-41d4-a716-446655440000", employeeId.id());
    }

    @Test
    void testEmployeeOfMethod() {
        Employee employee = Employee.of("550e8400-e29b-41d4-a716-446655440000", "Jane Doe", "297.957.120-20", "+55 21 98765-4321");
        assertNotNull(employee);
        assertEquals("550e8400-e29b-41d4-a716-446655440000", employee.getEmployeeId().id());
        assertEquals("Jane Doe", employee.getName());
        assertEquals("297.957.120-20", employee.getDocument());
        assertEquals("+55 21 98765-4321", employee.getCellphone());
    }

    @Test
    void testInvalidEmployeeId() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            new Employee.EmployeeId("invalid-uuid");
        });
        assertTrue(exception.getMessage().contains("id: O campo deve ser do tipo UUID"));
    }

    @Test
    void testInvalidCellphone() {
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            new Employee("John Doe", "297.957.120-20", "invalid-phone");
        });
        assertTrue(exception.getMessage().contains("Telefone inválido. O telefone deve seguir o padrão +XX XX XXXXX-XXXX"));
    }
}