package com.github.rafaelfernandes.notification.adapter.in;

import com.github.rafaelfernandes.notification.application.port.in.NotificationUseCase;
import com.github.rafaelfernandes.notification.common.annotations.MessageBroker;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;

@MessageBroker
@AllArgsConstructor
public class ConsumeMessageService {

    private final NotificationUseCase notificationUseCase;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(@Payload String deliveryId) {
        notificationUseCase.sendPackageArrivedNotification(deliveryId);
    }

}
