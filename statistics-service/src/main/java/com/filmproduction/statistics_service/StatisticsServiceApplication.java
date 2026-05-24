package com.filmproduction.statistics_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"controllers", "services", "domain", "infrastructure", "infrastracture", "com.filmproduction.statistics_service"})
@EnableJpaRepositories(basePackages = {"infrastructure", "infrastracture"})
@EntityScan(basePackages = {"infrastructure", "infrastracture"})
public class StatisticsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsServiceApplication.class, args);
    }
}