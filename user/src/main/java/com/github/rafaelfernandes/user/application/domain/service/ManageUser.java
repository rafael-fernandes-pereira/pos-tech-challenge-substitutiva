package com.github.rafaelfernandes.user.application.domain.service;

import com.github.rafaelfernandes.common.exceptions.UserCellphoneExistsException;
import com.github.rafaelfernandes.user.application.domain.model.Employee;
import com.github.rafaelfernandes.user.application.domain.model.Resident;
import com.github.rafaelfernandes.user.application.domain.model.User;
import com.github.rafaelfernandes.user.application.port.in.UserUseCase;
import com.github.rafaelfernandes.user.application.port.out.EmployeePort;
import com.github.rafaelfernandes.user.application.port.out.ResidentPort;
import com.github.rafaelfernandes.user.application.port.out.UserPort;
import com.github.rafaelfernandes.common.exceptions.ResidentApartmentExistsException;
import com.github.rafaelfernandes.common.exceptions.ResidentCellphoneExistsException;
import com.github.rafaelfernandes.common.annotations.UseCase;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class ManageUser implements UserUseCase {

    private final ResidentPort residentPort;
    private final EmployeePort employeePort;
    private final UserPort userPort;

    @Override
    public String createResident(Resident resident) {

        var user = userPort.findByCellphone(resident.getCellphone());

        if (user.isPresent()) throw new UserCellphoneExistsException(resident.getCellphone());

        Boolean residentFound = residentPort.exitsByCellphone(resident.getCellphone());

        if (residentFound) throw new UserCellphoneExistsException(resident.getCellphone());

        Boolean apartmentFound = residentPort.exitsByApartment(resident.getApartment());

        if (apartmentFound) throw new ResidentApartmentExistsException(resident.getApartment());

        var newResidentId = residentPort.save(resident);

        var newResident = residentPort.getById(newResidentId);

        var newUser = new User(newResident);

        var savedUser = userPort.save(newUser);

        return savedUser.getPassword();

    }

    @Override
    public String createEmployee(Employee employee) {

        var user = userPort.findByCellphone(employee.getCellphone());

        if (user.isPresent()) throw new UserCellphoneExistsException(employee.getCellphone());

        Boolean employeeFound = employeePort.exitsByCellphone(employee.getCellphone());

        if (employeeFound) throw new UserCellphoneExistsException(employee.getCellphone());

        var newEmployeeId = employeePort.save(employee);

        var newEmployee = employeePort.getById(newEmployeeId);

        var newUser = new User(newEmployee);

        var savedUser = userPort.save(newUser);

        return savedUser.getPassword();

    }
}
