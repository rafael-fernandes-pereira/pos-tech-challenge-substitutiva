package com.github.rafaelfernandes.gateway.exception;

public record ResponseError(
    String message, Integer status
) {
    
}
