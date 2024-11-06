//package org.example.customer.config;
//
//import com.cloudinary.Cloudinary;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class MyCloudinaryConfig {
//
//    @Value("${cloudinary.cloud-name}")
//    private String cloudName;
//
//    @Value("${cloudinary.api-key}")
//    private String apiKey;
//
//    @Value("${cloudinary.api-secret}")
//    private String apiSecret;
//
//    @Bean(name = "Cloudinary2")
//    public Cloudinary cloudinaryy() {
//        System.out.println("Cloud Name: " + cloudName);
//        System.out.println("API Key: " + apiKey);
//        System.out.println("API Secret: " + apiSecret);
//
//        Map<String, String> config = new HashMap<>();
//        config.put("cloud_name", cloudName);
//        config.put("api_key", apiKey);
//        config.put("api_secret", apiSecret);
//        return new Cloudinary(config);
//    }
//}