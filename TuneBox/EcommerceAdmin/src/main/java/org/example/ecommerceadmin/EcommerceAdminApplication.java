package org.example.ecommerceadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"org.example.library.*", "org.example.*", "org.example.ecommerceadmin.*"},exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories(value = "org.example.library.repository")
@EntityScan(value = "org.example.library.model")
public class EcommerceAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceAdminApplication.class, args);
    }

}
