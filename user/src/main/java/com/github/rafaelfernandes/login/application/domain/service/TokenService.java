package com.github.rafaelfernandes.login.application.domain.service;

import com.github.rafaelfernandes.common.annotations.UseCase;
import com.github.rafaelfernandes.common.enums.UserType;
import com.github.rafaelfernandes.common.exceptions.InvalidTokenException;
import com.github.rafaelfernandes.common.exceptions.UnauthorizedException;
import com.github.rafaelfernandes.login.application.port.in.TokenUseCase;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@UseCase
public class TokenService implements TokenUseCase {

    private Key key;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generateToken(String cellphone, UserType userType, UUID id) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("role", userType.name());
        claims.put("loginId", id);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(cellphone)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 120000))
                .signWith(this.key, SignatureAlgorithm.HS256).compact();

    }

    @Override
    public Boolean isInvalid(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        Date createdDate = claims.getIssuedAt();

        Date now = new Date();

        return now.getTime() - createdDate.getTime() > 120000;
    }

    @Override
    public void roleIsValid(String token, String role) {

        var claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        var roleClaim = claims.get("role");

        if (!roleClaim.equals(role)) {
            throw new InvalidTokenException();
        }

    }
}
