package com.movie.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */

@SpringBootApplication(scanBasePackages = {

        "com.movie.ticket",
        "com.movie.amqp",
        "com.movie.jwt",
        "com.movie.client"


})
@EnableFeignClients(basePackages = "com.movie.client")
@EnableDiscoveryClient
@EntityScan(basePackages = "com.movie.ticket.ticket.entity")



public class TicketApp
{
    public static void main(String[]args){
        SpringApplication.run(TicketApp.class,args);
    }
}

