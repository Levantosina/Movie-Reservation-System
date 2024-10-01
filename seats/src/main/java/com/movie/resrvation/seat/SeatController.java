package com.movie.resrvation.seat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
@Slf4j
@RestController
@RequestMapping(path = "api/v1/seats")
public class SeatController {

    private final  SeatService seatService;

    @PostMapping void registerNewSeat(@RequestBody SeatRegistrationRequest seatRegistrationRequest){
        log.info("New seat registration: {}", seatRegistrationRequest);
        seatService.registerNewSeat(seatRegistrationRequest);
    }

    public SeatController(SeatService seatService) {

        this.seatService = seatService;
    }
    @GetMapping
    public List<SeatDTO> getAllSeats(){
        return seatService.getAllSeats();
    }
    @GetMapping("{seatId}")
    public SeatDTO getSeat(@PathVariable("seatId")Long seatId){

        return seatService.getSeat(seatId);
    }
    @GetMapping("/cinema/{cinemaId}")
    public List<SeatDTO> getAllSeatsByCinemaId(@PathVariable Long cinemaId) {
        log.info("Fetching seats for cinema ID by cinemaId: {}", cinemaId);
        return seatService.getSeatsByCinema(cinemaId);
    }

    @PostMapping("/cinema")
    public List<SeatDTO> getSeatsByCinema(@RequestParam Long cinemaId) {
        log.info("Fetching seats for cinema ID: {}", cinemaId);
        return seatService.getSeatsByCinema(cinemaId);
    }


}
