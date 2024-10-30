package com.movie.client.cinemaClient;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * @author DMITRII LEVKIN on 04/10/2024
 * @project MovieReservationSystem
 */
@FeignClient(name = "cinemas",url = "http://localhost:8081")
@Component
public interface CinemaClient {
    @GetMapping("/api/v1/cinemas/name/{cinemaName}")
    void getCinemaByName(@PathVariable("cinemaName") String cinemaName);

    @GetMapping("/api/v1/cinemas/id-by-name/{cinemaName}")
    Long getCinemaIdByName(@PathVariable("cinemaName") String cinemaName);
}


