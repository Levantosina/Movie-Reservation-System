package com.movie.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



@SpringBootApplication(scanBasePackages = {
        "com.movie.notification",
        "com.movie.amqp"
})
@EnableDiscoveryClient
@EntityScan(basePackages = "com.movie.notification")
public class NotificationApp
{
    public static void main( String[] args )
    {

        SpringApplication.run(NotificationApp.class,args);
    }
}
