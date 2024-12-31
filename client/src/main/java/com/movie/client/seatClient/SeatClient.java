package com.movie.client.seatClient;


import com.movie.common.SeatDTO;
import com.movie.common.TotalSeatsDTO;
import com.movie.jwt.jwt.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author DMITRII LEVKIN on 04/10/2024
 * @project MovieReservationSystem
 */
@FeignClient(name = "seat",configuration = FeignConfig.class)
public interface SeatClient {
    @GetMapping(value= "/api/v1/seats/cinema/{cinemaId}/total-seats")
    TotalSeatsDTO getTotalSeatsByCinemaId(@PathVariable("cinemaId") Long cinemaId);

    @GetMapping(value = "/api/v1/seats/{id}")
    SeatDTO getSeatById(@PathVariable("id") Long seatId);


    @PostMapping("/{seatId}/update-occupation")
    ResponseEntity<?> updateSeatOccupation(@PathVariable("seatId") Long seatId, @RequestBody boolean isOccupied);

}
