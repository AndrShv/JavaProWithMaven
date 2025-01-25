package com.example.configuration.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "ce30f7c12414c49174783326a878ffdd37fa2cba30c46df365b3a11dc5b63888";

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, true);
    }
}
