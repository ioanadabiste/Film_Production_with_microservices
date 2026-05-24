package com.filmproduction.actor_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"controllers", "services", "infrastracture", "config", "com.filmproduction.actor_service"})
@EnableJpaRepositories(basePackages = "infrastracture")
@EntityScan(basePackages = "infrastracture")
public class ActorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActorServiceApplication.class, args);
    }
}