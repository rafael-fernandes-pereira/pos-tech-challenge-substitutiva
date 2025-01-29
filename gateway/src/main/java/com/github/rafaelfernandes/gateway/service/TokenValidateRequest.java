package com.github.rafaelfernandes.gateway.service;

public record TokenValidateRequest(
        String token,
        String role
) {
}
