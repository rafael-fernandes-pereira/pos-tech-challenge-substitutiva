package com.github.rafaelfernandes.user.application.port.in;

import com.github.rafaelfernandes.user.application.domain.model.Employee;
import com.github.rafaelfernandes.user.application.domain.model.Resident;

public interface UserUseCase {

    String createResident(Resident resident);

    String createEmployee(Employee employee);

}
