package com.api.campuslink.security;

import com.api.campuslink.security.filters.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService; // ServiceImpl class wll be injected

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    CustomAccessDeniedHandler accessDeniedHandler;
    @Autowired
    CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Value("${campusLink.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(customizer -> customizer.disable()) // Disabling the csrf
                .cors(Customizer.withDefaults()) // Enable CORS with default settings
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/login", "/api/auth/redirect","/api/user/add" ,"/api/user/*/add","/api/role/all", "/api/campus/all", "/api/user/exists").permitAll() // Only allow these route without authentication
                        .requestMatchers("/api/user/delete","/api/user/*/delete", "/api/user/*/delete/multiple").hasAnyAuthority("ROLE_ADMIN") // Only allow admin to access mentioned endpoints
                        .anyRequest().authenticated()) // Any request must be validated
                .exceptionHandling(expHandler -> {
                    expHandler.accessDeniedHandler(accessDeniedHandler)
                            .authenticationEntryPoint(authenticationEntryPoint);
                })
                .httpBasic(Customizer.withDefaults()) // Allow to access api with basic auth (i.e. username and password)
                .sessionManagement(session -> session.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Each request must be stateless, and the security context must be handled per request
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Before  UsernamePasswordAuthenticationFilter applying jwtFilter
                .build();
    }

    // CORS configuration to allow requests from frontend
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setAllowCredentials(true);
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Creating our own authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // for authentication using credentials from db
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10)); // specify which password encoder to user
        provider.setUserDetailsService(userDetailsService); // specify which user details service to use for verifying user
        return provider;
    }

    // This authentication manager bean will be used instead of default authentication manager
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
