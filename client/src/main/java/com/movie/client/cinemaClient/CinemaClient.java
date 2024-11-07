package com.movie.client.cinemaClient;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



/**
 * @author DMITRII LEVKIN on 04/10/2024
 * @project MovieReservationSystem
 */
@FeignClient(value = "cinema")

public interface CinemaClient {
    @GetMapping(value= "/api/v1/cinemas/id-by-name/{cinemaName}")
    Long getCinemaIdByName(@PathVariable("cinemaName") String cinemaName);
}


