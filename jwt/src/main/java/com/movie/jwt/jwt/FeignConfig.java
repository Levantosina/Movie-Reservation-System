package com.movie.jwt.jwt;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author DMITRII LEVKIN on 30/12/2024
 * @project Movie-Reservation-System
 */
@Configuration
public class FeignConfig {

@Autowired
private JWTUtil jwtUtil;

@Bean
public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
        String token = jwtUtil.getTokenFromSecurityContext();
        if (token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    };
}
}
