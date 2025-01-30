package com.github.rafaelfernandes.employee.application.port.in;

import com.github.rafaelfernandes.employee.application.domain.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EmployeeUseCase {

    Employee.EmployeeId create(Employee employee);

    Page<Employee> getAll(Pageable pageable);

    void delete(String residentId);

    void update(Employee.EmployeeId id, Employee employee);

    Employee findById(String residentId);

    Employee findByCellphone(String cellphone);

}
