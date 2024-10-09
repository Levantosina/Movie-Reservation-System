package com.movie.users.security;

import com.movie.users.users.UsersDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author DMITRII LEVKIN on 09/10/2024
 * @project MovieReservationSystem
 */
@Configuration
public class SecurityConfig {

    private final UsersDetailsService usersDetailsService;

    public SecurityConfig(UsersDetailsService usersDetailsService) {
        this.usersDetailsService = usersDetailsService;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/users/**").permitAll()
                                .anyRequest().authenticated()
                )
                .userDetailsService(usersDetailsService);  // Use custom UserDetailsService

        return http.build();
    }
}

