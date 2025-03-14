package com.example.configuration.testconfig;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.mockito.Mockito;

@Configuration
public class TestConfig {

    private RabbitTemplate rabbitTemplate;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return Mockito.mock(RabbitTemplate.class);
    }
}
