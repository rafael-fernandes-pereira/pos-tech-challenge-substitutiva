package com.github.rafaelfernandes.resident.application.domain.service;

import com.github.rafaelfernandes.resident.application.domain.model.Resident;
import com.github.rafaelfernandes.resident.application.port.in.ResidentUseCase;
import com.github.rafaelfernandes.resident.application.port.out.ManageResidentPort;
import com.github.rafaelfernandes.resident.common.annotations.UseCase;
import com.github.rafaelfernandes.resident.common.exception.ResidentApartmentExistsException;
import com.github.rafaelfernandes.resident.common.exception.ResidentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@UseCase
@RequiredArgsConstructor
public class ManageResidentService implements ResidentUseCase{

    private final ManageResidentPort manageResidentPort;

    @Override
    public Resident.ResidentId create(Resident resident) {

        var existsByApartment = manageResidentPort.existsByApartment(resident.getApartment());

        if (existsByApartment) {
            throw new ResidentApartmentExistsException(resident.getApartment());
        }

        return manageResidentPort.save(resident).getResidentId();


    }

    @Override
    public Page<Resident> getAll(Pageable pageable) {
        return manageResidentPort.getAll(pageable);
    }

    @Override
    public void delete(String residentId) {
        var resident = findById(residentId);

        manageResidentPort.delete(resident);


    }

    @Override
    public Boolean update(Resident resident) {
        return null;
    }

    @Override
    public Resident findById(String residentId) {
        return manageResidentPort.findById(residentId).orElseThrow(ResidentNotFoundException::new);
    }

    @Override
    public Resident findByApartment(Integer apartment) {
        return manageResidentPort.findByApartment(apartment).orElseThrow(ResidentNotFoundException::new);
    }
}
