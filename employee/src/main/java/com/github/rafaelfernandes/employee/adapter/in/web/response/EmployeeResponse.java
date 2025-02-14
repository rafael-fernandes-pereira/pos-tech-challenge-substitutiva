package com.github.rafaelfernandes.employee.adapter.in.web.response;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record EmployeeResponse(

        @Schema(implementation = UUID.class, example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(minimum = "3", maximum = "100", example = "Rafael Fernandes")
        String name,

        @Schema(minimum = "11", maximum = "14", example = "123.456.789-00")
        String document,

        @Schema(minimum = "17", maximum = "17", example = "+55 11 99999-9999")
        String cellphone
) {

    public static EmployeeResponse fromDomain(Employee employee) {
        return new EmployeeResponse(
                employee.getEmployeeId().id(),
                employee.getName(),
                employee.getDocument(),
                employee.getCellphone()
        );
    }
}
