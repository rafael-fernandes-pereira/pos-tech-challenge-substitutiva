package com.github.rafaelfernandes.login.adapter.in.web.request;

public record TokenValidateRequest(
        String token,
        String role
) {
}
