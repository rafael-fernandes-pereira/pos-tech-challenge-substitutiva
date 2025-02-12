package com.github.rafaelfernandes.delivery.adapter.out.api.employee.response;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record EmployeeResponse(

        @Schema(implementation = UUID.class)
        String id,

        @Schema(minimum = "3", maximum = "100")
        String name,

        @Schema(minimum = "11", maximum = "14")
        String document,

        @Schema(minimum = "17", maximum = "17")
        String cellphone
) {
}
