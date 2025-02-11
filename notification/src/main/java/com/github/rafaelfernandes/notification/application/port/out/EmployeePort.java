package com.github.rafaelfernandes.notification.application.port.out;



import com.github.rafaelfernandes.notification.application.domain.model.Employee;

import java.util.Optional;

public interface EmployeePort {

    Optional<Employee> findById(String employeeId);
}
