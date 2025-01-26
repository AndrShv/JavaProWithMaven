package com.example.configuration.testconfig;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mockito;

@Configuration
public class TestConfig {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return Mockito.mock(RabbitTemplate.class);
    }
}
