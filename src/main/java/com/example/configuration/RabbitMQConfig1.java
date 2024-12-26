package com.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig1 {

    @Bean
    public Queue exampleQueue1() {
        return new Queue("exampleQueue1", true);
    }
}
