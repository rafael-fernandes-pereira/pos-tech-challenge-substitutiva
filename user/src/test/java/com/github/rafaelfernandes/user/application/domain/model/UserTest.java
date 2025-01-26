package com.github.rafaelfernandes.user.application.domain.model;

import com.github.rafaelfernandes.common.enums.UserType;
import com.github.rafaelfernandes.common.utils.PasswordUtils;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testUserConstructor_ValidResident() {
        Resident resident = mock(Resident.class);



        try (MockedStatic<PasswordUtils> mockedPasswordUtils = mockStatic(PasswordUtils.class)) {
            mockedPasswordUtils.when(PasswordUtils::generatePassayPassword).thenReturn("ValidPass1!");

            User user = new User(resident);

            assertNotNull(user.getUserId());

            assertEquals(UserType.RESIDENT.name(), user.getUserType());
            assertEquals("ValidPass1!", user.getPassword());
            assertTrue(user.getResident().isPresent());
            assertEquals(resident, user.getResident().get());
        }
    }

    @Test
    public void testUserConstructor_EmptyPassword() {
        Resident resident = mock(Resident.class);


        assertThrows(ConstraintViolationException.class, () -> {
            try (MockedStatic<PasswordUtils> mockedPasswordUtils = mockStatic(PasswordUtils.class)) {
                mockedPasswordUtils.when(PasswordUtils::generatePassayPassword).thenReturn("");

                new User(resident);
            }
        });
    }

}