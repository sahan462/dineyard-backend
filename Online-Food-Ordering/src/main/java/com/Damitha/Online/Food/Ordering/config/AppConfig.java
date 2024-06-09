package com.Damitha.Online.Food.Ordering.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration // Marks this class as a configuration class for Spring
@EnableWebSecurity // Enables Spring Security for the application
public class AppConfig {

    @Bean // Defines this method as a bean to be managed by Spring
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configures the session management to be stateless
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configures authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Requires 'restaurantOwner' or 'admin' roles for accessing /api/admin/**
                        .requestMatchers("/api/admin/**").hasAnyRole("restaurantOwner", "admin")
                        // Requires authentication for all other /api/** endpoints
                        .requestMatchers("/api/**").authenticated()
                        // Permits all other requests without authentication
                        .anyRequest().permitAll()
                )
                // Adds a custom filter for JWT token validation before the basic authentication filter
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                // Disables CSRF protection (useful for stateless APIs)
                .csrf(csrf -> csrf.disable())
                // Configures Cross-Origin Resource Sharing (CORS)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // Returns the SecurityFilterChain instance configured by HttpSecurity
        return http.build();
    }

    // Defines a bean for CORS configuration
    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();

                // Sets the allowed origins for CORS requests
                cfg.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                // Allows all HTTP methods (GET, POST, PUT, DELETE, etc.)
                cfg.setAllowedMethods(Collections.singletonList("*"));
                // Allows credentials (cookies, authorization headers, etc.) to be sent with requests
                cfg.setAllowCredentials(true);
                // Allows all headers in CORS requests
                cfg.setAllowedHeaders(Collections.singletonList("*"));
                // Exposes the 'Authorization' header in the response
                cfg.setExposedHeaders(Arrays.asList("Authorization"));
                // Sets the max age for the CORS configuration to 3600 seconds (1 hour)
                cfg.setMaxAge(3600L);

                return cfg;
            }
        };
    }

    @Bean // Defines this method as a bean to be managed by Spring
    PasswordEncoder passwordEncoder() {
        // Returns a BCryptPasswordEncoder instance for encoding passwords
        return new BCryptPasswordEncoder();
    }
}

//======================Filter Chain============================
//In the context of web security and web applications, a filter chain refers to a series of filters that an HTTP request
//or response must pass through before reaching its destination (e.g., a controller or a resource) or being returned
//to the client. Each filter in the chain performs a specific function, such as authentication, authorization, logging, input validation, etc.

//In the context of web applications and security, "stateless" refers to a design where each request from a client to
//the server is independent and contains all the information needed to understand and process the request.
//The server does not retain any information (state) about previous requests.

//In Java, the -> symbol is known as the lambda operator or arrow operator. It is used to introduce a lambda expression,
//which is a feature introduced in Java 8. A lambda expression is a concise way to represent an anonymous function
//(a function without a name) that can be passed around and executed.