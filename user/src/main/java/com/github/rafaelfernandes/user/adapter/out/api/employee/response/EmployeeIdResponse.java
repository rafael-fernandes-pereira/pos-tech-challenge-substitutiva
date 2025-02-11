package com.github.rafaelfernandes.user.adapter.out.api.employee.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record EmployeeIdResponse(
        @Schema(implementation = UUID.class)
        UUID employee_id
) {
}
