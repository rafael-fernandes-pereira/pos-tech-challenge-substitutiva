package com.github.rafaelfernandes.notification.application.port.out;



import com.github.rafaelfernandes.notification.application.domain.model.Resident;

import java.util.Optional;

public interface ResidentPort {

    Optional<Resident> getByApartment(Integer apartment);

}
