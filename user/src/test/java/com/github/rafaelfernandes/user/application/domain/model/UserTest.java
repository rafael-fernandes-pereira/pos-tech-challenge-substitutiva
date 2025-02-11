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
        when(resident.getCellphone()).thenReturn("+12 34 56789-1234");

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
        when(resident.getCellphone()).thenReturn("+12 34 56789-1234");

        assertThrows(ConstraintViolationException.class, () -> {
            try (MockedStatic<PasswordUtils> mockedPasswordUtils = mockStatic(PasswordUtils.class)) {
                mockedPasswordUtils.when(PasswordUtils::generatePassayPassword).thenReturn("");

                new User(resident);
            }
        });
    }
    @Test
    public void testUserOfMethod_ValidParameters() {
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        String userType = UserType.RESIDENT.name();
        String cellphone = "+12 34 56789-1234";
        String password = "ValidPass1!";

        User user = User.of(userId, userType, cellphone, password);

        assertNotNull(user.getUserId());
        assertEquals(userId, user.getUserId().id());
        assertEquals(userType, user.getUserType());
        assertEquals(cellphone, user.getCellphone());
        assertEquals(password, user.getPassword());
    }


    @Test
    public void testUserConstructor_ValidEmployee() {
        Employee employee = mock(Employee.class);
        when(employee.getCellphone()).thenReturn("+12 34 56789-1234");

        try (MockedStatic<PasswordUtils> mockedPasswordUtils = mockStatic(PasswordUtils.class)) {
            mockedPasswordUtils.when(PasswordUtils::generatePassayPassword).thenReturn("ValidPass1!");

            User user = new User(employee);

            assertNotNull(user.getUserId());
            assertEquals(UserType.EMPLOYEE.name(), user.getUserType());
            assertEquals("ValidPass1!", user.getPassword());
            assertTrue(user.getEmployee().isPresent());
            assertEquals(employee, user.getEmployee().get());
        }
    }



    @Test
    public void testUserConstructorEmployee_EmptyPassword() {
        Employee employee = mock(Employee.class);
        when(employee.getCellphone()).thenReturn("+12 34 56789-1234");

        assertThrows(ConstraintViolationException.class, () -> {
            try (MockedStatic<PasswordUtils> mockedPasswordUtils = mockStatic(PasswordUtils.class)) {
                mockedPasswordUtils.when(PasswordUtils::generatePassayPassword).thenReturn("");

                new User(employee);
            }
        });
    }

    @Test
    public void testUserOfMethod_InvalidCellphone() {
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        String userType = UserType.RESIDENT.name();
        String invalidCellphone = "1234567890";
        String password = "ValidPass1!";

        assertThrows(ConstraintViolationException.class, () -> {
            User.of(userId, userType, invalidCellphone, password);
        });
    }

    @Test
    public void testUserOfMethod_EmptyPassword() {
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        String userType = UserType.RESIDENT.name();
        String cellphone = "+12 34 56789-1234";
        String emptyPassword = "";

        assertThrows(ConstraintViolationException.class, () -> {
            User.of(userId, userType, cellphone, emptyPassword);
        });
    }

}