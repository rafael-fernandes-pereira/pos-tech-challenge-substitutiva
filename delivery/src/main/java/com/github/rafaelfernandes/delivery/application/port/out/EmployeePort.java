package com.github.rafaelfernandes.delivery.application.port.out;

import com.github.rafaelfernandes.delivery.application.domain.model.Employee;

import java.util.Optional;

public interface EmployeePort {

    Optional<Employee> findByCellphone(String cellphone);

    Optional<Employee> findById(String employeeId);
}
