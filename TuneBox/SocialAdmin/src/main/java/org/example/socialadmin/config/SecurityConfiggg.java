package org.example.socialadmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiggg {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF protection
            .csrf(csrf -> csrf.disable())
            
            // Disable CORS 
            .cors(cors -> cors.disable())
            
            // Disable authentication for all requests
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()
            )
            
            // Disable login form
            .formLogin(form -> form.disable())
            
            // Disable HTTP Basic authentication
            .httpBasic(basic -> basic.disable())
            
            // Disable logout
            .logout(logout -> logout.disable())
            
            // Disable session management
            .sessionManagement(session -> session.disable());

        return http.build();
    }
}