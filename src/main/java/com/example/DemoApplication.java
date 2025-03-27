package com.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
@EntityScan(basePackages = "com.example.model")
@EnableJpaRepositories(basePackages = "com.example.repository")
@ComponentScan(basePackages = {"com.example", "com.example.configuration", "com.example.service", "com.example.rabbitMqConfigs"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
