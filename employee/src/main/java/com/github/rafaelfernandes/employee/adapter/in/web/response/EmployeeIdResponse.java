package com.github.rafaelfernandes.employee.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record EmployeeIdResponse(
        @Schema(implementation = UUID.class)
        UUID resident_id
) {
}
