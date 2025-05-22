package com.noyex.auth.config;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
        "com.noyex.auth",
        "com.noyex.data",
        "com.noyex.service"
})
public class AuthConfig {
}
