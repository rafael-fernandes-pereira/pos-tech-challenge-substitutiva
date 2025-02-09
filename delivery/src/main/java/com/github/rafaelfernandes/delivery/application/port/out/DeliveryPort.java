package com.github.rafaelfernandes.delivery.application.port.out;

import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.domain.model.Resident;

import java.util.List;

public interface DeliveryPort {

    Delivery.DeliveryId save(Delivery delivery);

    List<Delivery> getAllByResident(Resident resident);

}
