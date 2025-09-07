package com.example.demo.configuaration;

import org.hibernate.annotations.ConcreteProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class CustomFilterSercurity {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CORS (nếu muốn bật thì dùng .cors(Customizer.withDefaults()))
                .cors(cors -> cors.disable())

                // Tắt CSRF cho REST API (vì bạn dùng JWT)
                .csrf(csrf -> csrf.disable())

                // Phân quyền endpoint
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll() // cho phép không cần login
                        .anyRequest().authenticated() // các API khác phải có JWT
                );

        return http.build();
}}
