package com.ead.notification.consumers;

import com.ead.notification.dtos.NotificationRecordCommandDto;
import com.ead.notification.services.NotificationService;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * The Class NotificationConsumer
 *
 * @author Miguel Vilela Moraes Ribeiro
 * @since 11/02/2025
 */
@Component
public class NotificationConsumer {
    final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ead.broker.queue.notificationCommandQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${ead.broker.exchange.notificationCommandExchange}", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = "${ead.broker.key.notificationCommandKey}")
    )
    public void listen(@Payload NotificationRecordCommandDto notificationRecordCommandDto) {
        notificationService.saveNotification(notificationRecordCommandDto);
    }
}
