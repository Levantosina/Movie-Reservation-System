package com.movie.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */

@SpringBootApplication(scanBasePackages ={
         "com.movie.users",
        "com.movie.amqp"} )
@EntityScan(basePackages = "com.movie.users.roles.entity")
@EnableFeignClients(basePackages = "com.movie.client")
public class UserApp {
    public static void main(String[] args) {

        SpringApplication.run(UserApp.class, args);
    }
}