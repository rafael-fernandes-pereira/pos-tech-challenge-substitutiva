package com.github.rafaelfernandes.user.application.port.out;

import com.github.rafaelfernandes.user.application.domain.model.Employee;
import com.github.rafaelfernandes.user.application.domain.model.Resident;

public interface EmployeePort {

    Boolean exitsByCellphone(String cellphone);

    Employee getById(String id);

    String save(Employee employee);

}
