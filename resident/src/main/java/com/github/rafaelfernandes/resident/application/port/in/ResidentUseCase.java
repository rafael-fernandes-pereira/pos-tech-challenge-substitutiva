package com.github.rafaelfernandes.resident.application.port.in;

import com.github.rafaelfernandes.resident.application.domain.model.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface ResidentUseCase {

    Resident.ResidentId create(Resident resident);

    Page<Resident> getAll(Pageable pageable);

    void delete(String residentId);

    void update(Resident.ResidentId id, Resident resident);

    Resident findById(String residentId);

    Resident findByApartment(Integer apartment);



}
