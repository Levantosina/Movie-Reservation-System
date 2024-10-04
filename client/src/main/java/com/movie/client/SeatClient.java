package com.movie.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author DMITRII LEVKIN on 04/10/2024
 * @project MovieReservationSystem
 */
@FeignClient(name = "seats", url = "http://localhost:8082/api/v1/seats")
public interface SeatClient {

    @GetMapping("/cinema/{cinemaId}/total-seats")
    Integer getTotalSeatsByCinemaId(@PathVariable("cinemaId") Long cinemaId);
}
