package com.github.rafaelfernandes.employee.application.port.out;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ManageEmployeePort {

    Employee save(Employee employee);

    Page<Employee> getAll(Pageable pageable);

    Optional<Employee> findById(String residentId);

    void delete(Employee employee);

    void update(Employee.EmployeeId id, Employee employee);

    Optional<Employee> findByCellphone(String cellphone);

}
