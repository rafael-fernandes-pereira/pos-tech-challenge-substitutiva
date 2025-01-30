package com.github.rafaelfernandes.user.adapter.out.api.resident;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ResidentIdResponse(
        @Schema(implementation = UUID.class)
        UUID resident_id
) {
}
