package com.github.rafaelfernandes.delivery.adapter.out.broker;

import com.github.rafaelfernandes.delivery.application.port.out.NotificationPort;
import com.github.rafaelfernandes.delivery.common.annotations.MessageBroker;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

@MessageBroker
public class RabbitMqBroker implements NotificationPort {

    @Value("${rabbitmq.queue.name}")
    String queueName;

    @Value("${rabbitmq.exchange.name}")
    String exchangeName;

    @Value("${rabbitmq.exchange.routingKey}")
    String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqBroker(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void notifyPackge(String deliveryId) {
        rabbitTemplate.convertAndSend(queueName, deliveryId);
    }
}
