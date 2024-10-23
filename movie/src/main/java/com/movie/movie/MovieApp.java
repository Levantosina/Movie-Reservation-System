package com.movie.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {


        "com.movie.movie",
        "com.movie.amqp",
        "com.movie.users"
}
)

@EnableDiscoveryClient
@EntityScan(basePackages = "com.movie.movies.movie.entity")
public class MovieApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(MovieApp.class,args);
    }
}
