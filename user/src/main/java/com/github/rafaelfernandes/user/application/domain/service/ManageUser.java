package com.github.rafaelfernandes.user.application.domain.service;

import com.github.rafaelfernandes.user.application.domain.model.Resident;
import com.github.rafaelfernandes.user.application.domain.model.User;
import com.github.rafaelfernandes.user.application.port.in.UserUseCase;
import com.github.rafaelfernandes.user.application.port.out.ResidentPort;
import com.github.rafaelfernandes.user.application.port.out.UserPort;
import com.github.rafaelfernandes.user.common.exceptions.ResidentApartmentExistsException;
import com.github.rafaelfernandes.user.common.exceptions.ResidentCellphoneExistsException;
import com.github.rafaelfernandes.user.common.annotations.UseCase;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class ManageUser implements UserUseCase {

    private final ResidentPort residentPort;
    private final UserPort userPort;

    @Override
    public String createResident(Resident resident) {

        Boolean residentFound = residentPort.exitsByCellphone(resident.getCellphone());

        if (residentFound) throw new ResidentCellphoneExistsException(resident.getCellphone());

        Boolean apartmentFound = residentPort.exitsByApartment(resident.getApartment());

        if (apartmentFound) throw new ResidentApartmentExistsException(resident.getApartment());

        var newResidentId = residentPort.save(resident);

        var newResident = residentPort.getById(newResidentId);

        var newUser = new User(newResident);

        var savedUser = userPort.save(newUser);

        return savedUser.getPassword();

    }
}
