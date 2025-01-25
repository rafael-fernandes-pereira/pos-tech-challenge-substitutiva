package com.github.rafaelfernandes.resident.application.domain.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResidentTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidResidentCreation() {
        Resident resident = new Resident("John Doe", "12345678909", "+55 12 98765-4321", 101);
        assertNotNull(resident);
        assertNotNull(resident.getResidentId());
        assertEquals("John Doe", resident.getName());
        assertEquals("12345678909", resident.getDocument());
        assertEquals("+55 12 98765-4321", resident.getCellphone());
        assertEquals(101, resident.getApartment());
    }

    @Test
    void testInvalidNameLength() {
        Exception exception = assertThrows(Exception.class, () -> {
            new Resident("Jo", "12345678909", "+55 12 98765-4321", 101);
        });
        assertTrue(exception.getMessage().contains("O campo deve ter no minimo 3 e no maximo 100 caracteres"));
    }

    @Test
    void testInvalidCPF() {
        Exception exception = assertThrows(Exception.class, () -> {
            new Resident("John Doe", "1234567890A", "+55 12 98765-4321", 101);
        });
        assertTrue(exception.getMessage().contains("CPF inválido"));
    }

    @Test
    void testInvalidCellphone() {
        Exception exception = assertThrows(Exception.class, () -> {
            new Resident("John Doe", "12345678909", "123456789", 101);
        });
        assertTrue(exception.getMessage().contains("Telefone inválido. O telefone deve seguir o padrão +XX XX XXXXX-XXXX"));
    }

    @Test
    void testInvalidApartment() {
        Exception exception = assertThrows(Exception.class, () -> {
            new Resident("John Doe", "12345678909", "+55 12 98765-4321", -1);
        });
        assertTrue(exception.getMessage().contains("O campo deve ser maior que zero (0)"));
    }

    @Test
    void testResidentIdValidation() {
        Exception exception = assertThrows(Exception.class, () -> {
            Resident.ResidentId invalidId = new Resident.ResidentId("invalid-uuid");
        });
        assertTrue(exception.getMessage().contains("O campo deve ser do tipo UUID"));
    }

    @Test
    void testOfMethodValidCreation() {
        Resident resident = Resident.of("550e8400-e29b-41d4-a716-446655440000", "Jane Doe", "12345678909", "+55 12 98765-4321", 102);
        assertNotNull(resident);
        assertEquals("Jane Doe", resident.getName());
        assertEquals("12345678909", resident.getDocument());
        assertEquals("+55 12 98765-4321", resident.getCellphone());
        assertEquals(102, resident.getApartment());
        assertEquals("550e8400-e29b-41d4-a716-446655440000", resident.getResidentId().id());
    }

    @Test
    void testOfMethodInvalidId() {
        Exception exception = assertThrows(Exception.class, () -> {
            Resident.of("invalid-uuid", "Jane Doe", "12345678909", "+55 12 98765-4321", 102);
        });
        assertTrue(exception.getMessage().contains("O campo deve ser do tipo UUID"));
    }

    @Test
    void testOfMethodInvalidNameLength() {
        Exception exception = assertThrows(Exception.class, () -> {
            Resident.of("550e8400-e29b-41d4-a716-446655440000", "Jo", "12345678909", "+55 12 98765-4321", 102);
        });
        assertTrue(exception.getMessage().contains("O campo deve ter no minimo 3 e no maximo 100 caracteres"));
    }

    @Test
    void testOfMethodInvalidCPF() {
        Exception exception = assertThrows(Exception.class, () -> {
            Resident.of("550e8400-e29b-41d4-a716-446655440000", "Jane Doe", "1234567890A", "+55 12 98765-4321", 102);
        });
        assertTrue(exception.getMessage().contains("CPF inválido"));
    }

    @Test
    void testOfMethodInvalidCellphone() {
        Exception exception = assertThrows(Exception.class, () -> {
            Resident.of("550e8400-e29b-41d4-a716-446655440000", "Jane Doe", "12345678909", "123456789", 102);
        });
        assertTrue(exception.getMessage().contains("Telefone inválido. O telefone deve seguir o padrão +XX XX XXXXX-XXXX"));
    }

    @Test
    void testOfMethodInvalidApartment() {
        Exception exception = assertThrows(Exception.class, () -> {
            Resident.of("550e8400-e29b-41d4-a716-446655440000", "Jane Doe", "12345678909", "+55 12 98765-4321", -1);
        });
        assertTrue(exception.getMessage().contains("O campo deve ser maior que zero (0)"));
    }



}