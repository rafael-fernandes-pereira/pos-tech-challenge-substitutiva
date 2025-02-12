package delivery;

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
        String enterDate,
        String exitDate
) {

}
