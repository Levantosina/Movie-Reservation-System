package com.movie.cinema.security;


import com.movie.jwt.jwt.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
/**
 * @author DMITRII LEVKIN on 22/10/2024
 * @project MovieReservationSystem
 */
@Configuration
@EnableWebSecurity
public class CinemaSecurityFilterChainConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter JWTAuthenticationFilter;


    private  final AuthenticationEntryPoint authenticationEntryPoint;


    public CinemaSecurityFilterChainConfig(AuthenticationProvider authenticationProvider, JWTAuthenticationFilter JWTAuthenticationFilter, AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationProvider = authenticationProvider;
        this.JWTAuthenticationFilter = JWTAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(HttpMethod.GET, "/api/v1/cinemas/**")
                .permitAll()
                .requestMatchers("/api/v1/auth/**")
                .permitAll()
                .anyRequest().
                authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(JWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
        return http.build();
    }

}
