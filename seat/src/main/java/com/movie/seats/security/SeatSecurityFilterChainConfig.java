package com.movie.seats.security;


import com.movie.jwt.jwt.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
/**
 * @author DMITRII LEVKIN on 22/10/2024
 * @project MovieReservationSystem
 */
@Configuration
@EnableWebSecurity
public class SeatSecurityFilterChainConfig {


    private final JWTAuthenticationFilter JWTAuthenticationFilter;

    public SeatSecurityFilterChainConfig(com.movie.jwt.jwt.JWTAuthenticationFilter jwtAuthenticationFilter) {
        JWTAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/seats/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/seats/**").permitAll()//WHYYYYY???
                         .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().
                authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(JWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
