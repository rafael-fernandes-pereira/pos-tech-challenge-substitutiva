package com.github.rafaelfernandes.delivery.adapter.in.web.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DeliveryResponse(
        String id,
        Integer apartment,
        String deliveryStatus,
        String employeeName,
        String destinationName,
        String packageDescription,
        String receiverName,
        String notificationStatus,
        LocalDateTime enterDate,
        LocalDateTime exitDate
) {

}
