package com.movie.resrvation.cinema;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
@Slf4j
@RestController
@RequestMapping(path = "api/v1/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;

    }

    @GetMapping
    public List<CinemaDTO> getAllCinemas(){
        return cinemaService.getAllCinemas();
    }
    @GetMapping("/id/{cinemaId}")
    public CinemaDTO getCinema(@PathVariable("cinemaId")Long cinemaId){

        return cinemaService.getCinema(cinemaId);
    }

    @GetMapping("/name/{cinemaName}")
    public CinemaDTO getCinema(@PathVariable("cinemaName")String cinemaName){

        return cinemaService.getCinemaByName(cinemaName);
    }

    @PostMapping
    public void registerCinema(@RequestBody CinemaRegistrationRequest cinemaRegistrationRequest) {
        log.info("New customer registration: {}", cinemaRegistrationRequest);
        cinemaService.registerCinema(cinemaRegistrationRequest);
    }
}
