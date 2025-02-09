package com.github.rafaelfernandes.delivery.application.port.out;

import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.domain.model.Resident;
import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;

import java.util.List;
import java.util.Optional;

public interface DeliveryPort {

    Delivery.DeliveryId save(Delivery delivery);

    List<Delivery> getAllByResident(Resident resident);

    Optional<Delivery> getById(String deliveryId);

    void updateNotificationStatus(String deliveryId, NotificationStatus notificationStatus);

    void delivered(String deliveryId, String receiverName);

}
