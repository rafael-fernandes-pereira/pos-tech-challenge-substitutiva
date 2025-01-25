package com.github.rafaelfernandes.resident.adapter.in.web.response;

public record ResponseError(
        String message, Integer status
) {
}
