package com.movie.seats;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {

        "com.movie.seats",
        "com.movie.users",
        "com.movie.amqp",
        "com.movie.client"
})
@EnableDiscoveryClient
@EntityScan(basePackages = "com.movie.seats.seat.entity")
@EnableFeignClients(basePackages = "com.movie.client")
public class SeatApp
{
    public static void main( String[] args )
    {

        SpringApplication.run(SeatApp.class,args);
    }
}

