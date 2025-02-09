package com.github.rafaelfernandes.delivery.application.port.out;

import com.github.rafaelfernandes.delivery.application.domain.model.Resident;

import java.util.Optional;

public interface ResidentPort {

    Optional<Resident> getByApartment(Integer apartment);

}
