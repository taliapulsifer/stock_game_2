package com.example.demo.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //applies the CORS configuration to all endpoints in the application
                .allowedOrigins("http://localhost:3000") //This allows requests only from the REACT frontend running on
                //localhost:3000
                .allowedMethods("GET", "POST", "PUT", "DELETE") //Specifies what methods are allowed for CORS requests
                .allowedHeaders("*") //Allows all headers in CORS requests
                .allowCredentials(true); //This allows credentials (like cookies) to be sent with requests, if needed.
    }
}
