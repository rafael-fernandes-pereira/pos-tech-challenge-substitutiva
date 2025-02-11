package com.github.rafaelfernandes.notification.application.domain.service;

import com.github.rafaelfernandes.notification.application.port.in.NotificationUseCase;
import com.github.rafaelfernandes.notification.application.port.out.DeliveryPort;
import com.github.rafaelfernandes.notification.application.port.out.NotificationPort;
import com.github.rafaelfernandes.notification.common.annotations.UseCase;
import com.github.rafaelfernandes.notification.common.exception.DeliveryNotFoundException;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class NotificationService implements NotificationUseCase {

    private final DeliveryPort deliveryPort;

    private final NotificationPort notificationPort;

    @Override
    public void sendPackageArrivedNotification(String deliveryId) {

        var delivery = deliveryPort.getById(deliveryId);

        if (delivery.isEmpty()) throw new DeliveryNotFoundException(deliveryId);

        var message = """
                Prezado(a) %s, informamos que seu pacote '%s' para '%s' chegou.
                """.formatted(delivery.get().residentName(), delivery.get().packageDescription(), delivery.get().destinationName());

        notificationPort.notifyPackge(delivery.get().residentCellphone(), message);

        deliveryPort.sentNotification(deliveryId);

    }
}
