package com.example.bookhubbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 설정을 활성화
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/**").permitAll()  // 모든 요청을 인증 없이 허용
                )
                .csrf(AbstractHttpConfigurer::disable);  // CSRF 비활성화

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return null;
    }
}
