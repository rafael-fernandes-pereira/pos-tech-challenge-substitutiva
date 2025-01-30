package com.github.rafaelfernandes.employee.application.domain.service;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import com.github.rafaelfernandes.employee.application.port.in.EmployeeUseCase;
import com.github.rafaelfernandes.employee.application.port.out.ManageEmployeePort;
import com.github.rafaelfernandes.employee.common.annotations.UseCase;
import com.github.rafaelfernandes.employee.common.exception.EmployeeCellphoneExistsException;
import com.github.rafaelfernandes.employee.common.exception.EmployeeNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@UseCase
@AllArgsConstructor
public class EmployeeService implements EmployeeUseCase {

    private final ManageEmployeePort manageEmployeePort;

    @Override
    public Employee.EmployeeId create(Employee employee) {
        var employeeExists = manageEmployeePort.findByCellphone(employee.getCellphone());

        if (employeeExists.isPresent()) {
            throw new EmployeeCellphoneExistsException(employee.getCellphone());
        }

        return manageEmployeePort.save(employee).getEmployeeId();
    }

    @Override
    public Page<Employee> getAll(Pageable pageable) {
        return manageEmployeePort.getAll(pageable);
    }

    @Override
    public void delete(String residentId) {
        var employee = findById(residentId);

        manageEmployeePort.delete(employee);
    }

    @Override
    public void update(Employee.EmployeeId employeeId, Employee employee) {
        var employeeExists = manageEmployeePort.findById(employeeId.id());

        if (employeeExists.isEmpty()) {
            throw new EmployeeNotFoundException();
        }

        manageEmployeePort.update(employeeId, employee);
    }

    @Override
    public Employee findById(String residentId) {
        return manageEmployeePort.findById(residentId).orElseThrow(EmployeeNotFoundException::new);
    }

    @Override
    public Employee findByCellphone(String cellphone) {
        return manageEmployeePort.findByCellphone(cellphone).orElseThrow(EmployeeNotFoundException::new);
    }
}
