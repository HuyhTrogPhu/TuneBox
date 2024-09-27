package org.example.customer.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng cho tất cả các đường dẫn
                .allowedOrigins("http://localhost:3000") // Cho phép origin từ frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các phương thức
                .allowedHeaders("*"); // Cho phép tất cả các header
    }
}
