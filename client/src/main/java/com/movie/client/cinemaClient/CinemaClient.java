package com.movie.client.cinemaClient;

import com.movie.cinema.cinema.CinemaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author DMITRII LEVKIN on 04/10/2024
 * @project MovieReservationSystem
 */
@FeignClient(name = "cinemas", url = "http://localhost:8081/api/v1/cinemas")
public interface CinemaClient {
    @GetMapping("/name/{cinemaName}")
    CinemaDTO getCinemaByName(@PathVariable("cinemaName") String cinemaName);
}