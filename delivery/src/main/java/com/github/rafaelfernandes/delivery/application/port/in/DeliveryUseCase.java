package com.github.rafaelfernandes.delivery.application.port.in;

import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;

import java.util.List;

public interface DeliveryUseCase {

    Delivery.DeliveryId create(Integer apartment, String employeeCellphone, String nameDestination, String packageDescription);

    List<Delivery> getAllByApartment(Integer apartment, String deliveryStatus);

}
