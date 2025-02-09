package com.github.rafaelfernandes.delivery.adapter.out.persistence;

import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.port.out.DeliveryPort;
import com.github.rafaelfernandes.delivery.common.annotations.PersistenceAdapter;
import lombok.AllArgsConstructor;

@PersistenceAdapter
@AllArgsConstructor
public class DeliveryPersistenceAdapter implements DeliveryPort {

    private final DeliveryRepositpory deliveryRepositpory;

    @Override
    public Delivery.DeliveryId save(Delivery delivery) {

        var deliveryEntity = DeliveryJpaEntity.builder().
                residentId(delivery.getResident().getResidentId().id()).
                employeeId(delivery.getEmployee().getEmployeeId().id()).
                deliveryStatus(delivery.getDeliveryStatus()).
                notificationStatus(delivery.getNotificationStatus()).
                enterDate(delivery.getEnterDate()).
                receiverName(delivery.getReceiverName()).
                build();

        var saved = deliveryRepositpory.save(deliveryEntity);

        return new Delivery.DeliveryId(saved.getId().toString());

    }
}
