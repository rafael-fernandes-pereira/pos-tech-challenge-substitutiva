package com.github.rafaelfernandes.employee.adapter.in.web.response;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;
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

    public static EmployeeResponse fromDomain(Employee employee) {
        return new EmployeeResponse(
                employee.getEmployeeId().id(),
                employee.getName(),
                employee.getDocument(),
                employee.getCellphone()
        );
    }
}
