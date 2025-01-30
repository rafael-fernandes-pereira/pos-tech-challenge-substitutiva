package com.github.rafaelfernandes.user.adapter.out.api.employee;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmployeeRequest(
        @Schema(minimum = "3", maximum = "100")
        String name,

        @Schema(minimum = "11", maximum = "14")
        String document,

        @Schema(minimum = "17", maximum = "17")
        String cellphone

) {
}
