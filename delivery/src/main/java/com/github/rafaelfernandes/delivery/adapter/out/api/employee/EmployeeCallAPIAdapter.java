package com.github.rafaelfernandes.delivery.adapter.out.api.employee;

import com.github.rafaelfernandes.delivery.application.domain.model.Employee;
import com.github.rafaelfernandes.delivery.application.port.out.EmployeePort;
import com.github.rafaelfernandes.delivery.common.annotations.ApiClientAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApiClientAdapter
@RequiredArgsConstructor
public class EmployeeCallAPIAdapter implements EmployeePort {

    private final EmployeeApiClient employeeApiClient;

    @Override
    public Optional<Employee> findByCellphone(String cellphone) {

        var response = employeeApiClient.findByCellphone(cellphone);

        return response.map(employeeResponse -> Employee.of(
                employeeResponse.id(),
                employeeResponse.name(),
                employeeResponse.document(),
                employeeResponse.cellphone()

        ));

    }
}
