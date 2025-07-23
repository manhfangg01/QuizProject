package com.quiz.learning.Demo.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    // Danh sách các domain được phép
    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:3000",
            "https://quizproject-e9bc5.firebaseapp.com",
            "https://quizproject-e9bc5.web.app" // Nếu có custom domain
    );

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cấu hình các origin được phép
        configuration.setAllowedOrigins(ALLOWED_ORIGINS);

        // Các HTTP methods được phép
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Các headers được phép
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "x-no-retry",
                "X-CSRF-TOKEN"));

        // Các headers sẽ exposed cho client
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Set-Cookie"));

        // Cho phép gửi credentials (cookies, auth headers)
        configuration.setAllowCredentials(true);

        // Thời gian cache pre-flight request
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}