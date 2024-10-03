package org.example.customer.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc // Bật hỗ trợ MVC
public class WebConfig implements WebMvcConfigurer  {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/") // Áp dụng cho tất cả các đường dẫn
                .allowedOriginPatterns("http://localhost:3000") // Cho phép origin từ frontend
                .allowCredentials(true)  // Cho phép gửi cookie
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các phương thức
                .allowedHeaders("*"); // Cho phép tất cả các header
    }
}
