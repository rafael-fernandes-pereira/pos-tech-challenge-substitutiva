package com.github.rafaelfernandes.user.adapter.in.response;

public record ResponseError(
        String message, Integer status
) {
}
