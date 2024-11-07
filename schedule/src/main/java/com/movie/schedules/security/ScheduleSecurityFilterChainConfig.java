package com.movie.schedules.security;


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
public class ScheduleSecurityFilterChainConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter JWTAuthenticationFilter;


    private  final AuthenticationEntryPoint authenticationEntryPoint;

    public ScheduleSecurityFilterChainConfig(AuthenticationProvider authenticationProvider, com.movie.jwt.jwt.JWTAuthenticationFilter jwtAuthenticationFilter, AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationProvider = authenticationProvider;
        JWTAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/schedules/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/schedules/schedules/**").permitAll()
                        //.hasRole("ADMIN")
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(JWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
        return http.build();
    }
}
