package com.noyex.auth;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.fail;

@Disabled
@SpringBootTest
class AuthApplicationTests {

    @Test
    @Disabled
    void contextLoads() {
    }

    @Test
    void checkPostgresDriverIsLoadable() {
        String driverClassName = "org.postgresql.Driver";
        try {
            Class.forName(driverClassName);
            System.out.println("SUCCESS: PostgreSQL Driver (" + driverClassName + ") loaded successfully by Class.forName()!");
            // Jeśli chcesz, możesz tu dodać asercję, ale samo przejście bez wyjątku jest już dobrym znakiem
            // assertNotNull(Class.forName(driverClassName), "Driver class should be loadable");
        } catch (ClassNotFoundException e) {
            System.err.println("FAILURE: PostgreSQL Driver (" + driverClassName + ") NOT FOUND in classpath by Class.forName()!");
            e.printStackTrace(); // Wydrukuj pełny stos błędu
            fail("PostgreSQL Driver (" + driverClassName + ") NOT FOUND in classpath by Class.forName()!", e);
        }
    }
}
