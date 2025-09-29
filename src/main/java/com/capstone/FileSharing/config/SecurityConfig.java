package com.capstone.FileSharing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Password encoder for hashing user passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager bean so services can authenticate users
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF since we'll use JWT for security
                .csrf(csrf -> csrf.disable())
                // Stateless session management
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (register, login, WebSocket handshake)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        // Allow access to static resources and index.html
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/static/**",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        // Everything else requires authentication
                        .anyRequest().authenticated()
                );

        // Later, we can add a JWT filter here with http.addFilterBefore()

        return http.build();
    }
}
