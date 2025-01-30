package com.github.rafaelfernandes.user.adapter.out.api.employee;

import com.github.rafaelfernandes.common.annotations.ApiClientAdapter;
import com.github.rafaelfernandes.user.application.domain.model.Employee;
import com.github.rafaelfernandes.user.application.domain.model.Resident;
import com.github.rafaelfernandes.user.application.port.out.EmployeePort;
import lombok.RequiredArgsConstructor;

@ApiClientAdapter
@RequiredArgsConstructor
public class EmployeeCallAPIAdapter implements EmployeePort {

    private final EmployeeApiClient employeeApiClient;

    @Override
    public Boolean exitsByCellphone(String cellphone) {
        return employeeApiClient.findByCellphone(cellphone).id() != null;
    }


    @Override
    public String save(Employee resident) {

        var saved = employeeApiClient.create(new EmployeeRequest(
                resident.getName(),
                resident.getDocument(),
                resident.getCellphone()
        ));

        return saved.employee_id().toString();

    }

    @Override
    public Employee getById(String id) {

        var residentResponse = employeeApiClient.findById(id);

        return Employee.of(
                residentResponse.id(),
                residentResponse.name(),
                residentResponse.document(),
                residentResponse.cellphone()
        );
    }
}
