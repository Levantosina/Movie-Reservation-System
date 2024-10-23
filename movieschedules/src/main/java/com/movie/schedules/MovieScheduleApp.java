package com.movie.schedules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
        "com.movie.schedules",
        "com.movie.client",
    "com.movie.users",
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.movie.client")
@EntityScan(basePackages = "com.movie.schedules.movieschedules.entity")
public class MovieScheduleApp
{
    public static void main( String[] args )
    {

        SpringApplication.run(MovieScheduleApp.class,args);
    }
}
