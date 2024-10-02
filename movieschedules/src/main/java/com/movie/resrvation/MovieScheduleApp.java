package com.movie.resrvation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = "com.movie.resrvation.movieschedules.entity")
public class MovieScheduleApp
{
    public static void main( String[] args )
    {

        SpringApplication.run(MovieScheduleApp.class,args);
    }
}
