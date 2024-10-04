package com.movie.apigw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author DMITRII LEVKIN on 29/09/2024
 * @project MovieReservationSystem
 */

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGateWayApp
{
    public static void main( String[] args )
    {

        SpringApplication.run(ApiGateWayApp.class, args);
    }
}