package com.movie.cinema;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = "com.movie.resrvation.cinema.entity")
public class CinemaApp
{
    public static void main( String[] args )
    {

        SpringApplication.run(CinemaApp.class, args);
    }
}

