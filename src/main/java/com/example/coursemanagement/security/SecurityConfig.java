package com.example.coursemanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/", "/api/auth/register", "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/auth/profile").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/teachers/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/news/**").permitAll()

                // News management (Admin only)
                .requestMatchers(HttpMethod.POST, "/api/news").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/news/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/news/**").hasRole("ADMIN")

                // Contact endpoints
                .requestMatchers(HttpMethod.POST, "/api/contacts").permitAll()
                .requestMatchers("/api/contacts/**").hasRole("ADMIN")

                // Course management
                .requestMatchers(HttpMethod.POST, "/api/courses").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasAnyRole("ADMIN", "TEACHER")

                // Teacher management
                .requestMatchers("/api/teachers/**").hasRole("ADMIN")

                // Student management
                .requestMatchers("/api/students/**").hasRole("ADMIN")

                // User management
                .requestMatchers("/api/users/**").hasRole("ADMIN")

                // Registration
                .requestMatchers(HttpMethod.POST, "/api/registrations").hasRole("STUDENT")
                .requestMatchers(HttpMethod.GET, "/api/registrations").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/registrations/my-courses").hasRole("STUDENT")
                .requestMatchers(HttpMethod.GET, "/api/registrations/teacher-courses").hasRole("TEACHER")
                .requestMatchers(HttpMethod.GET, "/api/registrations/course/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.PUT, "/api/registrations/*/cancel").hasAnyRole("STUDENT", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/registrations/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
