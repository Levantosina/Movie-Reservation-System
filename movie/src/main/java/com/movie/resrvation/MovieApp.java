package com.movie.resrvation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = "com.movie.resrvation.movie.entity")
public class MovieApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(MovieApp.class,args);
    }
}
