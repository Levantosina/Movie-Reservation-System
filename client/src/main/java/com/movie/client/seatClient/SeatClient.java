package com.movie.client.seatClient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author DMITRII LEVKIN on 04/10/2024
 * @project MovieReservationSystem
 */
@FeignClient(name = "seat")
public interface SeatClient {
    @GetMapping(value= "/api/v1/seats/cinema/{cinemaId}/total-seats")
    int getTotalSeatsByCinemaId(@PathVariable("cinemaId") Long cinemaId);
}
