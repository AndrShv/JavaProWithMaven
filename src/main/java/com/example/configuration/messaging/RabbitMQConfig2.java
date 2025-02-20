package com.example.configuration.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig2 {

    @Bean
    public Queue exampleQueue2() {
        return new Queue("exampleQueue2", true);
    }
}