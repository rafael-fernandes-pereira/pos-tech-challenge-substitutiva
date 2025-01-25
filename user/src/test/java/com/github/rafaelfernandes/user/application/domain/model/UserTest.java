package com.github.rafaelfernandes.user.application.domain.model;

import com.github.rafaelfernandes.user.common.enums.UserType;
import com.github.rafaelfernandes.user.common.utils.PasswordUtils;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.UUID;

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
    public void testUserConstructor_ValidInput() {
        try (MockedStatic<PasswordUtils> mockedPasswordUtils = mockStatic(PasswordUtils.class)) {
            mockedPasswordUtils.when(PasswordUtils::generatePassayPassword).thenReturn("ValidPass1!");

            User user = new User("+12 34 56789-1234", UserType.RESIDENT.name());

            assertNotNull(user.getUserId());
            assertEquals("+12 34 56789-1234", user.getCellphone());
            assertEquals(UserType.RESIDENT.name(), user.getUserType());
            assertEquals("ValidPass1!", user.getPassword());
        }
    }

    @Test
    public void testUserConstructor_InvalidCellphone() {

        assertThrows(ConstraintViolationException.class, () -> {
            new User("12345", UserType.RESIDENT.name());
        });


    }

    @Test
    public void testUserConstructor_InvalidUserType() {

        assertThrows(ConstraintViolationException.class, () -> {
            new User("+12 34 56789-1234", "INVALID_TYPE");
        });
    }

    @Test
    public void testUserConstructor_EmptyPassword() {

        assertThrows(ConstraintViolationException.class, () -> {

            try (MockedStatic<PasswordUtils> mockedPasswordUtils = mockStatic(PasswordUtils.class)) {
                mockedPasswordUtils.when(PasswordUtils::generatePassayPassword).thenReturn("");

                new User("+12 34 56789-1234", UserType.RESIDENT.name());

            }

        });
    }

    @Test
    public void testUserIdConstructor_ValidInput() {
        User.UserId userId = new User.UserId(UUID.randomUUID().toString());
        assertNotNull(userId.id());
    }

    @Test
    public void testUserConstructor_WithAllParameters() {
        User.UserId userId = new User.UserId(UUID.randomUUID().toString());
        String cellphone = "+12 34 56789-1234";
        String userType = UserType.RESIDENT.name();
        String password = "ValidPass1!";

        User user = new User(userId, cellphone, userType, password);

        assertNotNull(user.getUserId());
        assertEquals(userId, user.getUserId());
        assertEquals(cellphone, user.getCellphone());
        assertEquals(userType, user.getUserType());
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testUserOfMethod() {
        User.UserId userId = new User.UserId(UUID.randomUUID().toString());
        String cellphone = "+12 34 56789-1234";
        String userType = UserType.RESIDENT.name();
        String password = "ValidPass1!";

        User user = User.of(userId, cellphone, userType, password);

        assertNotNull(user.getUserId());
        assertEquals(userId, user.getUserId());
        assertEquals(cellphone, user.getCellphone());
        assertEquals(userType, user.getUserType());
        assertEquals(password, user.getPassword());
    }


}