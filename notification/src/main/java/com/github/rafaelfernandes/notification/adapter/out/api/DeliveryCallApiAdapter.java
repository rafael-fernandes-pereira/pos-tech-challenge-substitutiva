package com.github.rafaelfernandes.notification.adapter.out.api;

import com.github.rafaelfernandes.notification.application.domain.model.Delivery;
import com.github.rafaelfernandes.notification.application.port.out.DeliveryPort;
import com.github.rafaelfernandes.notification.common.annotations.ApiClientAdapter;
import lombok.AllArgsConstructor;


import java.util.Optional;

@ApiClientAdapter
@AllArgsConstructor
public class DeliveryCallApiAdapter implements DeliveryPort {

    private final DeliveryApiClient deliveryApiClient;

    @Override
    public Optional<Delivery> getById(String deliveryId)  {
        var delivery = deliveryApiClient.findById(deliveryId);

        return delivery.map(deliveryDataResponse -> new Delivery(
                new Delivery.DeliveryId(deliveryId),
                deliveryDataResponse.getDestinationName(),
                deliveryDataResponse.getPackageDescription(),
                deliveryDataResponse.getResidentName(),
                deliveryDataResponse.getResidentCellphone()
        ));

    }

    @Override
    public void sentNotification(String deliveryId) {
        deliveryApiClient.sentNotification(deliveryId);
    }
}
