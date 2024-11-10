package org.example.customer.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "org.example.*")
public class UserConfiguration {

    @Autowired
    @Lazy
    private JwtFilter jwtFilter;
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceConfig();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http     .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowCredentials(true);
                    config.addAllowedHeader("*");
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/user/register", "/user/list-genre", "/user/list-inspired-by",
                                "/user/list-talent", "/customer/shop/**", "/customer/brand/**",
                                "/customer/category/**", "/customer/instrument/**", "/user/**","/api/**",
                                "/customer/tracks/**", "/customer/albums/", "/customer/playlist/**", "/oauth2/success").permitAll()
                        .requestMatchers("/customer/cart/**").hasRole("CUSTOMER")
                        .requestMatchers("/e-comAdmin/**").hasRole("ECOMADMIN") // Chỉ cho phép ecomadmin
                        .requestMatchers("/socialAdmin/**").hasRole("SocialAdmin") // Chỉ cho phép socialadmin
                        .requestMatchers("/oauth2/**").authenticated()
                        .anyRequest().permitAll()
                )

                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/user/oauth2/success", true) // Điều hướng sau khi đăng nhập thành công
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sử dụng JWT nên không cần Session
                )
                .authenticationManager(authenticationManager);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}


