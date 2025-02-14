package com.github.rafaelfernandes.delivery.adapter.in.web.response;

import com.github.rafaelfernandes.delivery.common.enums.DeliveryStatus;
import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryResponse(
        @Schema(implementation = UUID.class, example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(implementation = Integer.class)
        Integer apartment,

        @Schema(implementation = DeliveryStatus.class, example = "DELIVERED")
        String deliveryStatus,

        @Schema(minimum = "3", maximum = "100")
        String employeeName,

        @Schema(minimum = "3", maximum = "100")
        String destinationName,

        @Schema(minimum = "3", maximum = "100")
        String packageDescription,

        @Schema(minimum = "3", maximum = "100")
        String receiverName,

        @Schema(implementation = NotificationStatus.class, example = "SENT")
        String notificationStatus,

        @Schema(implementation = LocalDateTime.class, example = "2021-08-01T00:00:00")
        LocalDateTime enterDate,

        @Schema(implementation = LocalDateTime.class, example = "2021-08-01T00:00:00")
        LocalDateTime exitDate
) {

}
