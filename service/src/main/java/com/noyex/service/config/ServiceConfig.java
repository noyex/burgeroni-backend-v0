package com.noyex.service.config;

import com.noyex.service.utils.OrderNumberGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.noyex.data",
        "com.noyex.service",
        "com.noyex.client"
})
public class ServiceConfig {

    @Bean
    OrderNumberGenerator orderNumberGenerator() {
        return new OrderNumberGenerator();
    }
}