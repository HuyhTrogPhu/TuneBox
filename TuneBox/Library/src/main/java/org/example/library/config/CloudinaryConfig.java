package org.example.library.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dki3vque5");
        config.put("api_key", "377552846226673");
        config.put("api_secret", "RpZBqdViIKfp2Iy0lzJAD3FiGkg");

        return new Cloudinary(config);
    }
}
