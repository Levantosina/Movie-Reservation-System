package com.movie.jwt.jwt;

import com.movie.exceptions.CustomFeignErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author DMITRII LEVKIN on 30/12/2024
 * @project Movie-Reservation-System
 */
@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getCredentials() instanceof String jwtToken) {
                requestTemplate.header("Authorization", "Bearer " + jwtToken);
            }
        };
    }
}
