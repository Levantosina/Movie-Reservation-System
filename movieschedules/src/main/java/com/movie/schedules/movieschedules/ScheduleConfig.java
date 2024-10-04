package com.movie.schedules.movieschedules;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author DMITRII LEVKIN on 03/10/2024
 * @project MovieReservationSystem
 */

@Configuration
public class ScheduleConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){

        return new RestTemplate();
    }
}


