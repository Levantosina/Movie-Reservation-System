package com.movie.resrvation.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class CinemaApp
{
    public static void main( String[] args )
    {

        SpringApplication.run(CinemaApp.class, args);
    }
}

