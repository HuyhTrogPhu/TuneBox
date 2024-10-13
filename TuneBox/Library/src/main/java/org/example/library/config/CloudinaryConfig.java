package org.example.library.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "dslm1fjny",
                "api_key", "687813197331129",
                "api_secret", "tGla5A5ZY2Jqp24sfTHtEN1ATds"
        );
        return new Cloudinary(config);
    }
}
