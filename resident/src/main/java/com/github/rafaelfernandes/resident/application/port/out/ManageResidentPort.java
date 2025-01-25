package com.github.rafaelfernandes.resident.application.port.out;

import com.github.rafaelfernandes.resident.application.domain.model.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ManageResidentPort {

    Boolean existsByApartment(Integer apartment);

    Resident save(Resident resident);

    Page<Resident> getAll(Pageable pageable);

    Optional<Resident> findById(String residentId);

    Optional<Resident> findByApartment(Integer apartment);

    void delete(Resident resident);

}
