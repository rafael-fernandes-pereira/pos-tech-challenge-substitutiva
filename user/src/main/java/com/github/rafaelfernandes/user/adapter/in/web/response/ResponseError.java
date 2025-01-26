package com.github.rafaelfernandes.user.adapter.in.web.response;

public record ResponseError(
        String message, Integer status
) {
}
