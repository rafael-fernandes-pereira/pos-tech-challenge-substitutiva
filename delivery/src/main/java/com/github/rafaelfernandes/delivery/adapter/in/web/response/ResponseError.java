package com.github.rafaelfernandes.delivery.adapter.in.web.response;

public record ResponseError(
        String message, Integer status
) {
}
