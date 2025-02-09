package com.github.rafaelfernandes.delivery.application.port.out;

import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;

public interface DeliveryPort {

    Delivery.DeliveryId save(Delivery delivery);

}
