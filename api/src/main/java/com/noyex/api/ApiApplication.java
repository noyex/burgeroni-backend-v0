package com.noyex.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.noyex")
@ComponentScan(basePackages = {
        "com.noyex.data",
        "com.noyex.service",
        "com.noyex.api",
        "com.noyex.client",
        "com.noyex.auth"
})
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
