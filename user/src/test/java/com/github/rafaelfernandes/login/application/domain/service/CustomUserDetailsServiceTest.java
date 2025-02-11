package com.github.rafaelfernandes.login.application.domain.service;

import com.github.rafaelfernandes.common.enums.UserType;
import com.github.rafaelfernandes.common.utils.PasswordUtils;
import com.github.rafaelfernandes.login.application.domain.model.CustomUserDetails;
import com.github.rafaelfernandes.login.application.domain.model.Login;
import com.github.rafaelfernandes.login.application.domain.model.LoginHash;
import com.github.rafaelfernandes.login.application.port.out.LoginPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private LoginPort loginPort;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private String password;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        password = PasswordUtils.generatePassayPassword();
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        var cellPhone = "+55 11 12345-7890";


        var loginHash = new LoginHash(
                UUID.randomUUID().toString(),
                UserType.EMPLOYEE.name(),
                cellPhone,
                password);

        when(loginPort.findByCellphone(cellPhone)).thenReturn(Optional.of(loginHash));

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(cellPhone);

        assertNotNull(userDetails);
        assertEquals(cellPhone, userDetails.getUsername());
        verify(loginPort).findByCellphone(cellPhone);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "1234567890";

        when(loginPort.findByCellphone(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(username));

        verify(loginPort).findByCellphone(username);
    }
}