package com.movie.client.seatClient;


import com.movie.common.SeatDTO;
import com.movie.common.TotalSeatsDTO;

import com.movie.jwt.jwt.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author DMITRII LEVKIN on 04/10/2024
 * @project MovieReservationSystem
 */
@FeignClient(name = "seat",configuration = FeignConfig.class)
public interface SeatClient {
    @GetMapping(value = "/api/v1/seats/cinema/{cinemaId}/total-seats")
    TotalSeatsDTO getTotalSeatsByCinemaId(@PathVariable("cinemaId") Long cinemaId);

    @GetMapping(value = "/api/v1/seats/{id}")
    SeatDTO getSeatById(@PathVariable("id") Long seatId);

    @PutMapping("/api/v1/seats/{seatId}/update-occupation")
    void updateSeatOccupation(@PathVariable("seatId") Long seatId, @RequestBody boolean isOccupied);

    @GetMapping("/api/v1/seats/{seatId}/price")
    BigDecimal getSeatPriceById(@PathVariable("seatId") Long seatId);

    @GetMapping("/api/v1/seats/price/{seatType}")
    BigDecimal getSeatPriceByType(@PathVariable("seatType") String seatType);

    @GetMapping("/api/v1/seats/cinema/{cinemaId}")
    List<SeatDTO> getSeatsByCinema(@PathVariable("cinemaId") Long cinemaId);

}


