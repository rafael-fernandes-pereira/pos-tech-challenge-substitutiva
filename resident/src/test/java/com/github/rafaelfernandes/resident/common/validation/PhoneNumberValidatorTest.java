package com.github.rafaelfernandes.resident.common.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class PhoneNumberValidatorTest {

    private PhoneNumberValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new PhoneNumberValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    public void testValidPhoneNumber() {
        assertTrue(validator.isValid("+123 45 12345-6789", context));
    }

    @Test
    public void testInvalidPhoneNumber() {
        assertFalse(validator.isValid("123-456-7890", context));
    }

    @Test
    public void testNullPhoneNumber() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    public void testEmptyPhoneNumber() {
        assertFalse(validator.isValid("", context));
    }
}
