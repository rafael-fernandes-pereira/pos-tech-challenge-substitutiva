package com.github.rafaelfernandes.login.application.domain.service;

import com.github.rafaelfernandes.common.enums.UserType;
import com.github.rafaelfernandes.common.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(tokenService, "secret", "mysecretkeymysecretkeymysecretkeymysecretkey");
    }

    @Test
    public void testInit() {
        tokenService.init();
        Key key = (Key) ReflectionTestUtils.getField(tokenService, "key");
        assertNotNull(key);
    }

    @Test
    public void testGenerateToken() {
        tokenService.init();
        String cellphone = "1234567890";
        UserType userType = UserType.EMPLOYEE;
        UUID id = UUID.randomUUID();

        String token = tokenService.generateToken(cellphone, userType, id);

        assertNotNull(token);

        Claims claims = Jwts.parserBuilder().setSigningKey((Key) ReflectionTestUtils.getField(tokenService, "key")).build().parseClaimsJws(token).getBody();

        assertEquals(cellphone, claims.getSubject());
        assertEquals(userType.name(), claims.get("role"));
        assertEquals(id.toString(), claims.get("loginId"));
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    public void testIsInvalid() {
        tokenService.init();
        String cellphone = "1234567890";
        UserType userType = UserType.EMPLOYEE;
        UUID id = UUID.randomUUID();

        // Generate a valid token
        String validToken = tokenService.generateToken(cellphone, userType, id);
        assertFalse(tokenService.isInvalid(validToken));

        Key key = (Key) ReflectionTestUtils.getField(tokenService, "key");

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", userType.name());
        claims.put("loginId", id);

        // Generate an expired token
        String expiredToken = Jwts.builder()
                .setSubject(cellphone)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis() - 130000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(key)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> tokenService.isInvalid(expiredToken));



    }

    @Test
    public void testRoleIsValid_ValidRole() {
        tokenService.init();
        String cellphone = "1234567890";
        UserType userType = UserType.EMPLOYEE;
        UUID id = UUID.randomUUID();

        String token = tokenService.generateToken(cellphone, userType, id);

        assertDoesNotThrow(() -> tokenService.roleIsValid(token, userType.name()));
    }

    @Test
    public void testRoleIsValid_InvalidRole() {
        tokenService.init();
        String cellphone = "1234567890";
        UserType userType = UserType.EMPLOYEE;
        UUID id = UUID.randomUUID();

        String token = tokenService.generateToken(cellphone, userType, id);

        assertThrows(InvalidTokenException.class, () -> tokenService.roleIsValid(token, UserType.RESIDENT.name()));
    }

}