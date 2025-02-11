package com.github.rafaelfernandes.notification.application.port.out;


import com.github.rafaelfernandes.notification.application.domain.model.Delivery;

import java.util.Optional;

public interface DeliveryPort {

    Optional<Delivery> getById(String deliveryId) ;

    void sentNotification(String deliveryId);

}
