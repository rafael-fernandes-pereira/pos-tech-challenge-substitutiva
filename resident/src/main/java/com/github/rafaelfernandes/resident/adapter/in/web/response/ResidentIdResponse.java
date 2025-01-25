package com.github.rafaelfernandes.resident.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ResidentIdResponse(
        @Schema(implementation = UUID.class)
        UUID resident_id
) {
}
