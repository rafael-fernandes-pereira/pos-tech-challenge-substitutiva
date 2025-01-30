package com.github.rafaelfernandes.employee.adapter.in.web.response;

public record ResponseError(
        String message, Integer status
) {
}
