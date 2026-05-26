package com.universite.plateforme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;

@SpringBootApplication
public class PlateformeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlateformeApplication.class, args);
    }
}
