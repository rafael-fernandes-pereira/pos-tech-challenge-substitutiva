package com.github.rafaelfernandes.delivery.adapter.out.api.employee;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record EmployeeIdResponse(
        @Schema(implementation = UUID.class)
        UUID employee_id
) {
}
