package com.github.rafaelfernandes.user.application.port.out;

import com.github.rafaelfernandes.user.application.domain.model.Resident;

import java.util.Optional;

public interface ResidentPort {

    Boolean exitsByCellphone(String cellphone);

    Boolean exitsByApartment(Integer apartment);

    Resident getById(String id);

    String save(Resident resident);

}
