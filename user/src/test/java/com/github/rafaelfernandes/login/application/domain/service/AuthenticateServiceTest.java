package com.github.rafaelfernandes.login.application.domain.service;

import com.github.rafaelfernandes.common.enums.UserType;
import com.github.rafaelfernandes.common.exceptions.UnauthorizedException;
import com.github.rafaelfernandes.common.utils.PasswordUtils;
import com.github.rafaelfernandes.login.application.domain.model.Login;
import com.github.rafaelfernandes.login.application.domain.model.LoginHash;
import com.github.rafaelfernandes.login.application.port.in.TokenUseCase;
import com.github.rafaelfernandes.login.application.port.out.LoginPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticateServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenUseCase tokenUseCase;

    @Mock
    private LoginPort loginPort;

    @InjectMocks
    private AuthenticateService authenticateService;

    private Login login;

    private String password;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        password = PasswordUtils.generatePassayPassword();
        login = new Login("+55 11 12345-7890", password);
    }

    @Test
    public void testAuthenticate_Success() {

        var loginHash = new LoginHash(
                UUID.randomUUID().toString(),
                UserType.EMPLOYEE.name(),
                "+55 11 12345-7890",
                password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(loginPort.findByCellphone(login.getCellphone()))
                .thenReturn(Optional.of(loginHash));
        when(tokenUseCase.generateToken(loginHash.getCellphone(), loginHash.getUserType(), loginHash.getUserId()))
                .thenReturn("token");

        String token = authenticateService.authenticate(login);

        assertNotNull(token);
        assertEquals("token", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(loginPort).findByCellphone(login.getCellphone());
        verify(tokenUseCase).generateToken(loginHash.getCellphone(), loginHash.getUserType(), loginHash.getUserId());
    }

    @Test
    public void testAuthenticate_BadCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(UnauthorizedException.class, () -> authenticateService.authenticate(login));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(loginPort, never()).findByCellphone(anyString());
        verify(tokenUseCase, never()).generateToken(anyString(), any(UserType.class), any(UUID.class));
    }

    @Test
    public void testAuthenticate_UserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(loginPort.findByCellphone(login.getCellphone()))
                .thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authenticateService.authenticate(login));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(loginPort).findByCellphone(login.getCellphone());
        verify(tokenUseCase, never()).generateToken(anyString(), any(UserType.class), any(UUID.class));
    }

    @Test
    public void testIsInvalid_ValidToken() {
        String token = "validToken";

        when(tokenUseCase.isInvalid(token)).thenReturn(false);

        authenticateService.isInvalid(token);

        verify(tokenUseCase).isInvalid(token);
    }


    @Test
    public void testIsInvalid_InvalidToken() {
        String token = "invalidToken";

        when(tokenUseCase.isInvalid(token)).thenReturn(true);

        assertThrows(UnauthorizedException.class, () -> authenticateService.isInvalid(token));

        verify(tokenUseCase).isInvalid(token);
    }
}