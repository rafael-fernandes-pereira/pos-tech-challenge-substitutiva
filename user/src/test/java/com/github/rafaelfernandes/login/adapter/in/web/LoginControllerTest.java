package com.github.rafaelfernandes.login.adapter.in.web;

import com.github.rafaelfernandes.common.exceptions.UnauthorizedException;
import com.github.rafaelfernandes.common.utils.PasswordUtils;
import com.github.rafaelfernandes.login.adapter.in.web.request.LoginRequest;
import com.github.rafaelfernandes.login.adapter.in.web.request.TokenValidateRequest;
import com.github.rafaelfernandes.login.adapter.in.web.response.LoginTokenResponse;
import com.github.rafaelfernandes.login.application.domain.model.Login;
import com.github.rafaelfernandes.login.application.port.in.AuthenticateUseCase;
import com.github.rafaelfernandes.login.application.port.in.TokenUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private AuthenticateUseCase authenticateUseCase;

    @Mock
    private TokenUseCase tokenUseCase;

    @InjectMocks
    private LoginController loginController;

    private String password;
    private String cellphone;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        password = PasswordUtils.generatePassayPassword();
        cellphone = "+55 12 98765-4321";
    }

    @Test
    public void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest(cellphone, password);
        String token = "token";

        when(authenticateUseCase.authenticate(any(Login.class))).thenReturn(token);

        ResponseEntity<LoginTokenResponse> response = loginController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(token, response.getBody().token());

    }

    @Test
    public void testLogin_Unauthorized() {
        LoginRequest loginRequest = new LoginRequest(cellphone, password);
        Login login = new Login(loginRequest.cellphone(), loginRequest.password());

        when(authenticateUseCase.authenticate(any(Login.class))).thenThrow(new UnauthorizedException());

        assertThrows(UnauthorizedException.class, () -> loginController.login(loginRequest));


    }

    @Test
    public void testValidateToken_Success() {
        String token = "validToken";
        String role = "USER";
        TokenValidateRequest request = new TokenValidateRequest(token, role);

        doNothing().when(authenticateUseCase).isInvalid(token);
        doNothing().when(tokenUseCase).roleIsValid(token, role);

        ResponseEntity<Boolean> response = loginController.validateToken(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody());
        verify(authenticateUseCase).isInvalid(token);
        verify(tokenUseCase).roleIsValid(token, role);
    }

    @Test
    public void testValidateToken_InvalidToken() {
        String token = "invalidToken";
        String role = "USER";
        TokenValidateRequest request = new TokenValidateRequest(token, role);

        doThrow(new UnauthorizedException()).when(authenticateUseCase).isInvalid(token);

        assertThrows(UnauthorizedException.class, () -> loginController.validateToken(request));

        verify(authenticateUseCase).isInvalid(token);
        verify(tokenUseCase, never()).roleIsValid(anyString(), anyString());
    }
}