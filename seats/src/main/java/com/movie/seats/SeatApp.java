package com.movie.seats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {

        "com.movie.seats",
        "com.movie.users"
})
@EnableDiscoveryClient
@EntityScan(basePackages = "com.movie.seats.seat.entity")
@EnableFeignClients
public class SeatApp
{
    public static void main( String[] args )
    {

        SpringApplication.run(SeatApp.class,args);
    }
}
