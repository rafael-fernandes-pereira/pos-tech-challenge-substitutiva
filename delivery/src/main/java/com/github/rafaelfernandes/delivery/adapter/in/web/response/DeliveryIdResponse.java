package com.github.rafaelfernandes.delivery.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record DeliveryIdResponse(
        @Schema(implementation = UUID.class, example = "123e4567-e89b-12d3-a456-426614174000")
        String id
) {
}
