package com.api.campuslink.security;

import com.api.campuslink.security.filters.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService; // ServiceImpl class wll be injected

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(customizer -> customizer.disable()) // disabling the csrf
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/add/user/student", "/add/user", "/api/auth/login").permitAll() // only allow these route without authentication
                        .anyRequest().authenticated()) // any request must be validated
                .httpBasic(Customizer.withDefaults()) // allow to access api with basic auth (i.e. username and password)
                .sessionManagement(session -> session.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // each request must be stateless, and the security context must be handled per request
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // before  UsernamePasswordAuthenticationFilter applying jwtFilter
                .build();
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
