package com.movie.users;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DMITRII LEVKIN on 25/11/2024
 * @project Movie-Reservation-System
 */
@RestController
public class TestController {
    record PingPong(String res){}

    @GetMapping("/ping")
    public PingPong getPingPong(){
        return new PingPong("pong");
    }
}
