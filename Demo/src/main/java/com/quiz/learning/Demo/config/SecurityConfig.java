package com.quiz.learning.Demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

        private final CustomAuthExceptionHandler customAuthExceptionHandler;
        private final CorsConfig corsConfig;
        private final JwtDecoder jwtDecoder;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
        @Value("${Base64-Secret}")
        private String secretKey;

        public SecurityConfig(CustomAuthExceptionHandler customAuthExceptionHandler, CorsConfig corsConfig,
                        JwtDecoder jwtDecoder, JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.customAuthExceptionHandler = customAuthExceptionHandler;
                this.corsConfig = corsConfig;
                this.jwtDecoder = jwtDecoder;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;

        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
                return configuration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {
                JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
                authoritiesConverter.setAuthoritiesClaimName("authorities");
                authoritiesConverter.setAuthorityPrefix("");
                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
                return jwtAuthenticationConverter;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                String[] whiteList = {
                                "/", "/api/auth/login", "/api/auth/refresh", "/api/auth/signup",
                                "/api/auth/request-reset-link",
                                "/api/auth/reset-password",
                                "api/auth/social-login",
                                "/api/auth/me",
                                "/storage/**",
                                "/api/client/quizzes/fetch",
                };

                String[] authenList = {
                                "/api/client/answers/**",
                                "/api/client/users/profile/**",
                                "/api/client/users/update-profile",
                                "/api/client/results/**",
                                "/api/client/quizzes/submit",
                                "/api/client/quizzes/save-progress",
                                "/api/client/quizzes/exit" };

                http
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .authorizeHttpRequests(auth -> auth
                                                // .dispatcherTypeMatchers(DispatcherType.FORWARD,
                                                // DispatcherType.INCLUDE).permitAll()
                                                .requestMatchers(whiteList).permitAll()
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Phân quyền ADMIN
                                                .requestMatchers(authenList).authenticated()
                                                .anyRequest().authenticated())
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(customAuthExceptionHandler)
                                                .accessDeniedHandler(customAuthExceptionHandler))
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                                                jwtAuthenticationConverter())))
                                .formLogin(f -> f.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }

}
