package com.example.demo.Configurations;

import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

@Configuration //Indicates that a class contains configuration settings, typically for beans and dependency injection
public class AppConfig {
    @Bean //Marks a methods as producing a bean to be managed by the Spring container. In this case restTemplate()
    //creates a reusable RestTemplate instance for making HTTP requests
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
