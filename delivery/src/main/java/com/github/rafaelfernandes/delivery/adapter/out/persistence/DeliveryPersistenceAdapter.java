package com.github.rafaelfernandes.delivery.adapter.out.persistence;

import com.github.rafaelfernandes.delivery.application.domain.model.Delivery;
import com.github.rafaelfernandes.delivery.application.domain.model.Resident;
import com.github.rafaelfernandes.delivery.application.port.out.DeliveryPort;
import com.github.rafaelfernandes.delivery.common.annotations.PersistenceAdapter;
import lombok.AllArgsConstructor;

import java.util.List;

@PersistenceAdapter
@AllArgsConstructor
public class DeliveryPersistenceAdapter implements DeliveryPort {

    private final DeliveryRepositpory deliveryRepositpory;

    @Override
    public Delivery.DeliveryId save(Delivery delivery) {

        var deliveryEntity = DeliveryMapper.toEntity(delivery);

        var saved = deliveryRepositpory.save(deliveryEntity);

        return new Delivery.DeliveryId(saved.getId().toString());

    }

    @Override
    public List<Delivery> getAllByResident(Resident resident) {
        var deliveries = deliveryRepositpory.findByResidentId(resident.residentId().id());

        return deliveries.stream().map(DeliveryMapper::toDomain).toList();


    }
}
